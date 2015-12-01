package com.specenergocontrol.ui.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.specenergocontrol.R;
import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.model.Zone;
import com.specenergocontrol.ui.activity.TasksActivity;
import com.specenergocontrol.utils.RealmHelper;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by Комп on 21.08.2015.
 */
public class FillTaskFragmetn extends AsyncFragment {

    private static final int MENU_ITEM_CANCEL = 0;
    private static final int MENU_ITEM_CAMERA = 1;
    private static final int MENU_ITEM_GALERY = 2;
    private static final int SELECT_PICTURE = 0x0001;
    private static final int CAPTURE_PICTURE_INTENT = 0x0002;
    private static final int SEND_AVATAR_REQUEST = 0x00010;

    private static final String EXTRA_TASK = "extra_task";
    private TaskModel task;
    private EditText accountEditText;
    private ImageView photoImageView;
    private Button photoButton;
    private LinearLayout layoutZones;
    private ArrayList<View> zonesLayoutList = new ArrayList<>();
    private Uri mCapturedImageURI;
    private EditText nameEditText;
    private Button saveButton;
    private boolean hasPhoto = false;
    private EditText commentEditText;

    public static FillTaskFragmetn getInstance(String taskID) {
        Bundle args = new Bundle();
        args.putString(EXTRA_TASK, taskID);

        FillTaskFragmetn fragment = new FillTaskFragmetn();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fill_task, container, false);
        String taskId= getArguments().getString(EXTRA_TASK);
        task = RealmHelper.loadTask(getActivity(), taskId).get(0);
        initViews(v);
        initClickListeners();
        ((ActionBarActivity)getActivity()).setTitle("(" + task.getAccount() + ")");
        ((TasksActivity)getActivity()).setDrawerIndicatorEnabled(false);
        return v;
    }

    private void initClickListeners() {
        registerForContextMenu(photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoButton.showContextMenu();
            }
        });
    }

    private void initViews(View v) {
        accountEditText = (EditText) v.findViewById(R.id.fill_task_account_edit_text);
        accountEditText.setVisibility(View.GONE);
        nameEditText = (EditText) v.findViewById(R.id.fill_task_name_edit_text);
        commentEditText = (EditText) v.findViewById(R.id.fill_task_comment_edit_text);
        photoImageView = (ImageView) v.findViewById(R.id.fill_task_image_preview);
        photoButton = (Button) v.findViewById(R.id.fill_task_image_button);
        saveButton = (Button) v.findViewById(R.id.fill_task_save_button);
        layoutZones = (LinearLayout) v.findViewById(R.id.fill_task_zones_layout);
        for (Zone zone : task.getZones()) {
            View zoneLayout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_zone, layoutZones, false);
            ((TextInputLayout)zoneLayout.findViewById(R.id.fill_task_value_text_input_layout)).setHint(zone.getName());
            layoutZones.addView(zoneLayout);
            zonesLayoutList.add(zoneLayout);
            if (!TextUtils.isEmpty(zone.getValue()))
                ((EditText)zoneLayout.findViewById(R.id.fill_task_value_edit_text)).setText(zone.getValue());
        }

        accountEditText.setText(task.getAccount());
        nameEditText.setText(task.getUserName());
        if (!TextUtils.isEmpty(task.getPhotoFilePath())) {
            ImageLoader.getInstance().displayImage(Uri.fromFile(new File(task.getPhotoFilePath())).toString(), photoImageView);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = checkZonesPeriod();
                if (!correct)
                    return;
                saveTask();
                Toast.makeText(getActivity(), R.string.save_confirmed, Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean checkZonesPeriod() {
        for (int i = 0; i < zonesLayoutList.size(); i++) {
            EditText zoneEditText = (EditText)zonesLayoutList.get(i).findViewById(R.id.fill_task_value_edit_text);
            String zoneValue = zoneEditText.getText().toString();
            Zone zone = task.getZones().get(i);
            if (zoneValue.length() == zone.getPeriod()) {
                zone.setValue(zoneValue);
            } else {
                showZoneAlertDialog(zone, zoneEditText);
                return false;
            }
        }
        return true;
    }

    private void showZoneAlertDialog(final Zone zone, final EditText zoneEditText) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setMessage(getString(R.string.zones_of_device_equals_to, zoneEditText.length()))
        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                zoneEditText.setText("");
                zoneEditText.requestFocus();
            }
        })
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setMeteringDiviceScale(zoneEditText);
                saveButton.callOnClick();
            }
        }).create();
        alertDialog.show();
    }

    private void setMeteringDiviceScale(EditText zoneEditText) {
        Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();
        task.setMeteringDeviceScale(zoneEditText.getText().length());
        realm.copyToRealmOrUpdate(task);
        realm.commitTransaction();
    }

    private void saveTask() {
        Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();
        if (hasPhoto) {
            task.setPhotoFilePath(getPath(mCapturedImageURI));
        }
        task.setComment(commentEditText.getText().toString());
        task.setVisitDate(new Date(System.currentTimeMillis()));
        realm.copyToRealmOrUpdate(task);
        RealmHelper.saveZones(getActivity(), task, realm);
        realm.commitTransaction();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.fill_task_image_button:
                menu.add(0, MENU_ITEM_CAMERA, 0, getString(R.string.upload_from_camera));
                menu.add(0, MENU_ITEM_CANCEL, 0, getString(android.R.string.cancel));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // пункты меню для tvColor
            case MENU_ITEM_CANCEL:

                break;
            case MENU_ITEM_CAMERA:
                String fileName = "temp.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                startActivityForResult(intent, CAPTURE_PICTURE_INTENT);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == CAPTURE_PICTURE_INTENT) {
                String capturedImageFilePath = getPath(mCapturedImageURI);
                if (capturedImageFilePath==null){
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
                } else {
                    hasPhoto = true;
                    ImageLoader.getInstance().displayImage(mCapturedImageURI.toString(), photoImageView);
                }
            }
        }
    }

    public String getPath(Uri contentURI) {
        // just some safety built in
        if( contentURI == null ) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
