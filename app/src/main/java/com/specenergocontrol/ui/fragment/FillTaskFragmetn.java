package com.specenergocontrol.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
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
import com.rey.material.widget.ProgressView;
import com.specenergocontrol.R;
import com.specenergocontrol.comands.Command;
import com.specenergocontrol.comands.CommandCallback;
import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.model.Zone;
import com.specenergocontrol.ui.activity.TasksActivity;
import com.specenergocontrol.utils.RealmHelper;
import com.specenergocontrol.utils.StreetEntityUtils;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import io.realm.Realm;

/**
 * Created by Комп on 21.08.2015.
 */
public class FillTaskFragmetn extends AsyncFragment {

    private static final int MENU_ITEM_CANCEL = 0;
    private static final int MENU_ITEM_CAMERA = 1;
    private static final int SELECT_PICTURE = 0x0001;
    private static final int CAPTURE_PICTURE_INTENT = 0x0002;
    private static final int SEND_AVATAR_REQUEST = 0x00010;

    private static final String EXTRA_TASK = "extra_task";
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 0x001;
    public static final int MENU_ITEM_OTHER_ONE = 3;
    public static final int MENU_ITEM_OTHER_TWO = 4;
    public static final int MENU_ITEM_OTHER_THREE = 5;
    public static final int MENU_ITEM_OTHER_FOUR = 6;
    private TaskModel task;
    private EditText accountEditText;
    private ImageView photoImageView;
    private LinearLayout layoutZones;
    private ArrayList<View> zonesLayoutList = new ArrayList<>();
    private Uri mCapturedImageURI;
    private EditText nameEditText;
    private Button saveButton;
    private boolean hasPhoto = false;
    private EditText commentEditText;
    private Button errorButton;

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
        registerForContextMenu(photoImageView);
        registerForContextMenu(errorButton);
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hasCameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                int hasWritePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (Build.VERSION.SDK_INT >= 23 && hasCameraPermission != PackageManager.PERMISSION_GRANTED
                        && hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }
                photoImageView.showContextMenu();
            }
        });
        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorButton.showContextMenu();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    photoImageView.showContextMenu();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "permissions Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initViews(View v) {
        progressBar = (ProgressView) v.findViewById(R.id.fill_task_progress);
        content = v.findViewById(R.id.fill_task_content);
        accountEditText = (EditText) v.findViewById(R.id.fill_task_account_edit_text);
        accountEditText.setVisibility(View.GONE);
        nameEditText = (EditText) v.findViewById(R.id.fill_task_name_edit_text);
        commentEditText = (EditText) v.findViewById(R.id.fill_task_comment_edit_text);
        errorButton = (Button) v.findViewById(R.id.fill_task_error_button);

        photoImageView = (ImageView) v.findViewById(R.id.fill_task_image_preview);
        saveButton = (Button) v.findViewById(R.id.fill_task_save_button);
        layoutZones = (LinearLayout) v.findViewById(R.id.fill_task_zones_layout);
        boolean focusSetted = false;
        for (Zone zone : task.getZones()) {
            View zoneLayout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_zone, layoutZones, false);
            ((TextInputLayout)zoneLayout.findViewById(R.id.fill_task_value_text_input_layout)).setHint(zone.getName());
            layoutZones.addView(zoneLayout);
            zonesLayoutList.add(zoneLayout);
            EditText editText = (EditText) zoneLayout.findViewById(R.id.fill_task_value_edit_text);
            if (!TextUtils.isEmpty(zone.getValue())) {
                editText.setText(zone.getValue());
            }
            if (!focusSetted) {
                focusSetted = true;
                editText.requestFocus();
            }
        }

        accountEditText.setText(task.getAccount());
        nameEditText.setText(task.getUserName());
        if (!TextUtils.isEmpty(task.getPhotoFilePath())) {
            ImageLoader.getInstance().displayImage(Uri.fromFile(new File(task.getPhotoFilePath())).toString(), photoImageView);
        }
        if (!TextUtils.isEmpty(task.getComment())) {
            commentEditText.setText(task.getComment());
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = checkZonesPeriod();
                if (!correct)
                    return;
                saveTask();
            }
        });

    }

    private boolean checkZonesPeriod() {
        for (int i = 0; i < zonesLayoutList.size(); i++) {
            EditText zoneEditText = (EditText)zonesLayoutList.get(i).findViewById(R.id.fill_task_value_edit_text);
            String zoneValue = zoneEditText.getText().toString();
            if (TextUtils.isEmpty(zoneValue)) {
                Toast.makeText(getActivity(), R.string.empty_value_error, Toast.LENGTH_LONG).show();
                return false;
            }
            Zone zone = task.getZones().get(i);
            if (zoneValue.length() == task.getMeteringDeviceScale()) {
                Realm realm = Realm.getInstance(getActivity());
                realm.beginTransaction();
                zone.setValue(zoneValue);
                realm.copyToRealmOrUpdate(zone);
                realm.commitTransaction();
            } else {
                showZoneAlertDialog(zoneEditText);
                return false;
            }
        }
        return true;
    }

    private void showZoneAlertDialog(final EditText zoneEditText) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
        .setMessage(getString(R.string.zones_of_device_equals_to, zoneEditText.length()))
        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
        showProgress(true);
        Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();
        if (hasPhoto) {
            task.setPhotoFilePath(getPath(mCapturedImageURI));
        }
        task.setComment(commentEditText.getText().toString());
        task.setVisitDate(new Date(System.currentTimeMillis()));
        task.setFilled(true);
        realm.copyToRealmOrUpdate(task);
        RealmHelper.saveZones(getActivity(), task, realm);
        realm.commitTransaction();

        StreetEntityUtils.createStreetEntities(getActivity());
        Toast.makeText(getActivity(), R.string.save_confirmed, Toast.LENGTH_LONG).show();
        getActivity().onBackPressed();
        showProgress(false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.fill_task_image_preview:
                menu.add(0, MENU_ITEM_CAMERA, 0, getString(R.string.upload_from_camera));
                menu.add(0, MENU_ITEM_CANCEL, 0, getString(android.R.string.cancel));
                break;
            case R.id.fill_task_error_button:
                menu.add(0, MENU_ITEM_OTHER_ONE, 0, getString(R.string.other_one));
                menu.add(0, MENU_ITEM_OTHER_TWO, 0, getString(R.string.other_two));
                menu.add(0, MENU_ITEM_OTHER_THREE, 0, getString(R.string.other_three));
                menu.add(0, MENU_ITEM_OTHER_FOUR, 0, getString(R.string.other_four));
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
            case MENU_ITEM_OTHER_ONE:
                setOtherReason(-1);
                break;
            case MENU_ITEM_OTHER_TWO:
                setOtherReason(-2);
                break;
            case MENU_ITEM_OTHER_THREE:
                setOtherReason(-3);
                break;
            case MENU_ITEM_OTHER_FOUR:
                setOtherReason(-4);
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

    private void setOtherReason(int reason) {
        ArrayList<Zone> zones = task.getZones();
        for (int i = 0; i < zones.size(); i++) {
            Zone zone = zones.get(i);
            Realm realm = Realm.getInstance(getActivity());
            realm.beginTransaction();
            zone.setValue(String.valueOf(reason));
            realm.copyToRealmOrUpdate(zone);
            realm.commitTransaction();
        }
        saveTask();
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
