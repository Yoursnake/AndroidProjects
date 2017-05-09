package com.example.shengliyi.criminalintentclass.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.shengliyi.criminalintentclass.entity.Crime;
import com.example.shengliyi.criminalintentclass.entity.CrimeLab;
import com.example.shengliyi.criminalintentclass.R;
import com.example.shengliyi.criminalintentclass.utils.PictureUtils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeFragment extends Fragment {

    private static final String ARGUMENTS_CRIME_ID = "com.example.shengliyi.criminalintentclass.arguments_crime_id";
    private static final String DIALOG_DATE = "dialog_date";
    private static final String DIALOG_TIME = "dialog_time";
    private static final String DIALOG_PHOTO = "dialog_photo";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;
    private static final int REQUEST_IMAGE = 4;
    private Crime mCrime;
    private EditText mCrimeTitle;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckedBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private boolean mCanTakePhoto = false;
    private ViewTreeObserver mObserver;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
        void onCrimeDeleted(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID) getArguments().getSerializable(ARGUMENTS_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.getInstance(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mCrimeTitle = (EditText) view
                .findViewById(R.id.crime_title_edit_text);
        mDateButton = (Button) view
                .findViewById(R.id.crime_date_button);
        mTimeButton = (Button) view
                .findViewById(R.id.crime_time_button);
        mSolvedCheckedBox = (CheckBox) view
                .findViewById(R.id.crime_solved_check_box);
        mReportButton = (Button) view
                .findViewById(R.id.crime_report);
        mSuspectButton = (Button) view
                .findViewById(R.id.crime_suspect);
        mCallButton = (Button) view
                .findViewById(R.id.crime_call_button);
        mPhotoButton = (ImageButton) view
                .findViewById(R.id.photo_button);
        mPhotoView = (ImageView) view
                .findViewById(R.id.crime_photo);
        mObserver = mPhotoView.getViewTreeObserver();

        // 用于匹配联系人的 Intent
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        不让任何联系人应用和你的设备匹配
//        pickContact.addCategory(Intent.CATEGORY_HOME);

        PackageManager packageManager = getActivity().getPackageManager();
        // 如果找不到 Activity 则禁用 Suspect 按钮，防止程序崩溃
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        // 如果有拍摄路径及拍摄软件则能够拍照
        if (mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null) {
            mCanTakePhoto = true;
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mPhotoButton.setEnabled(mCanTakePhoto);

        mCrimeTitle.setText(mCrime.getTitle());
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment
                        .newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        updateTime();
        // 如果版本号小于等于 23 则取消修改时间的功能
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
            mTimeButton.setVisibility(View.GONE);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment
                        .newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(fm, DIALOG_TIME);
            }
        });

        mSolvedCheckedBox.setChecked(mCrime.isSolved());
        mSolvedCheckedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrime.setSolved(mSolvedCheckedBox.isChecked());
                updateCrime();
            }
        });

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                intent.putExtra(Intent.EXTRA_SUBJECT,
//                        getString(R.string.crime_subject));
//                intent = Intent.createChooser(intent, getString(R.string.send_report));
                /*
                可用 IntentBuilder 便捷地创建 ACTION_SEND 的 intent
                 */
                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .createChooserIntent();
                startActivity(intent);
            }
        });

        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri telephoneNumber = Uri.parse("tel:" + mCrime.getTelephoneNumber());
                Intent intent = new Intent(Intent.ACTION_DIAL, telephoneNumber);
                startActivity(intent);
            }
        });

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        updatePhotoView();

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PhotoImageFragment photoImageFragment = PhotoImageFragment
                        .newInstance(mPhotoFile);
                photoImageFragment.setTargetFragment(CrimeFragment.this, REQUEST_IMAGE);
                photoImageFragment.show(fm, DIALOG_PHOTO);
            }
        });

        mObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                updatePhotoView();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                mCallbacks.onCrimeDeleted(mCrime);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDate() {
        Date date = mCrime.getDate();
        mDateButton.setText(DateFormat.format("yyyy MM dd E", date));
    }

    private void updateTime() {
        Date date = mCrime.getDate();
        mTimeButton.setText(DateFormat.format("hh:mm a", date));
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            // 将对图片处理的延时操作新开一个线程进行操作
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = PictureUtils
                            .getScaledBitmap(mPhotoFile.getPath(), getActivity());
                    setImageView(bitmap);
                }
            }).start();

        }
    }

    // 设置 photoView 的图片
    private void setImageView(final Bitmap bitmap) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPhotoView.setImageBitmap(bitmap);
            }
        });
    }

    private void updateCrime() {
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat
                .format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DATE:
                if (resultCode == Activity.RESULT_OK) {
                    Date date = (Date) data
                            .getSerializableExtra(DatePickerFragment.EXTRA_DIALOG_DATE);
                    mCrime.setDate(date);
                    updateDate();
                    updateCrime();
                }break;
            case REQUEST_TIME:
                if (resultCode == Activity.RESULT_OK) {
                    Date date = (Date) data
                            .getSerializableExtra(TimePickerFragment.EXTRA_DIALOG_TIME);
                    mCrime.setDate(date);
                    updateTime();
                    updateCrime();
                }break;
            case REQUEST_CONTACT:
                if (data != null) {
                    Uri contactUri = data.getData(); // 先得到 Uri
                    String[] queryFields = new String[]{
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts._ID
                    };
                    Cursor cursor = getActivity().getContentResolver()
                            .query(contactUri, queryFields, null, null, null);
                    Cursor telephoneCursor = null;
//                    Cursor c2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
                    try {
                        if (cursor.getCount() == 0)
                            return;
                        cursor.moveToFirst();

//                        String suspect = cursor.getString(cursor
//                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String suspect = cursor.getString(0);
                        mCrime.setSuspect(suspect);
                        updateCrime();
                        mSuspectButton.setText(suspect);

//                        String id = cursor.getString(cursor
//                                .getColumnIndex(ContactsContract.Contacts._ID));
                        String id = cursor.getString(1);
                        telephoneCursor = getActivity().getContentResolver()
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
                                        new String[] { id },
                                        null);
                        if (telephoneCursor.moveToFirst()) {
                            String telephoneNum = telephoneCursor.getString(telephoneCursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            mCrime.setTelephoneNumber(telephoneNum);
                            updateCrime();
                        }
                    } finally {
                        cursor.close();
                        telephoneCursor.close();
                    }
                }break;
            case REQUEST_PHOTO:
                updatePhotoView();
                updateCrime();
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static CrimeFragment newInstance(UUID id) {
        
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENTS_CRIME_ID, id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
