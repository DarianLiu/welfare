package com.geek.newmanager.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.geek.newmanager.R;
import com.geek.newmanager.Utils.BitmapUtil;
import com.geek.newmanager.Utils.FileSizeUtil;
import com.geek.newmanager.Utils.PermissionUtils;
import com.geek.newmanager.Utils.PictureHelper;
import com.geek.newmanager.di.component.DaggerUploadComponent;
import com.geek.newmanager.di.module.UploadModule;
import com.geek.newmanager.mvp.contract.UploadContract;
import com.geek.newmanager.mvp.model.entity.UploadCaseFile;
import com.geek.newmanager.mvp.model.entity.UploadFile;
import com.geek.newmanager.mvp.presenter.UploadPresenter;
import com.geek.newmanager.mvp.ui.adapter.UploadPhotoAdapter;
import com.geek.newmanager.mvp.ui.adapter.UploadVideoAdapter;
import com.geek.newmanager.view.CommProgressDailog;
import com.geek.newmanager.view.SelectDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class UploadActivity extends BaseActivity<UploadPresenter> implements UploadContract.View {
    public static final int imag_width = 200;
    public static final int imag_height = 250;
    private static final int REQUEST_CODE_ALBUM = 1; //打开相册4.4以前
    private static final int REQUEST_CODE_ALBUM_PHOTO = 2; //打开相册4.4以后
    private static final int RESULT_CAMERA = 3;  //打开相机
    public static final int RESULT_PHOTO = 4;  //跳转到图片显示界面
    public static final int RESULT_VIDEO = 5;  //视频拍摄,整改前
    public static final int RESULT_VIDEO_later = 6;  //视频拍摄,整改后S

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerView_later)
    RecyclerView recyclerView_later;

    @BindView(R.id.tv_photo_list)
    TextView tv_photo_list;
    @BindView(R.id.tv_photo_list_later)
    TextView tv_photo_list_later;
    @BindView(R.id.tv_video_list)
    TextView tv_video_list;
    @BindView(R.id.tv_video_list_later)
    TextView tv_video_list_later;

    private RxPermissions rxPermissions;
    private UploadPhotoAdapter adapter; //照片整改前
    private UploadPhotoAdapter adapter_later;//照片整改后
    private UploadVideoAdapter adapter_video;//视频整改前
    private UploadVideoAdapter adapter_video_later;//视频整改后
    private List<UploadFile> uploadPhotoList;  //整改前
    private List<UploadFile> uploadPhotoList_later;  //整改后
    private boolean isBeforePhoto; //照片整改前
    private List<UploadFile> videoList;  //视频拍摄  整改前
    private List<UploadFile> videoList_later;  //视频拍摄  整改后

    //视频

    @BindView(R.id.recyclerView_video)
    RecyclerView recyclerView_video;
    @BindView(R.id.recyclerView_video_later)
    RecyclerView recyclerView_video_later;

    private int caseId;
    private int entry_type;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUploadComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .uploadModule(new UploadModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_upload_test; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        entry_type = getIntent().getIntExtra("entry_type", 0);

        //屏蔽7.0中 拍照使用 Uri.fromFile爆出的FileUriExposureException
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= 24) {
            builder.detectFileUriExposure();
        }
        String caseIdString = getIntent().getStringExtra("case_id");
        if (!TextUtils.isEmpty(caseIdString)) {
            caseId = Integer.parseInt(caseIdString);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager_later = new LinearLayoutManager(this);
        recyclerView_later.addItemDecoration(divider);
        recyclerView_later.setLayoutManager(layoutManager_later);

        LinearLayoutManager layoutManager_video = new LinearLayoutManager(this);
        recyclerView_video.addItemDecoration(divider);
        recyclerView_video.setLayoutManager(layoutManager_video);
        LinearLayoutManager layoutManager_video_later = new LinearLayoutManager(this);
        recyclerView_video_later.addItemDecoration(divider);
        recyclerView_video_later.setLayoutManager(layoutManager_video_later);
    }

    //整改前
    private void recyclerViewAdapter() {
        if (adapter == null) {
            adapter = new UploadPhotoAdapter(this, uploadPhotoList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (uploadPhotoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    uploadPhotoList.remove(position);
                    adapter.notifyDataSetChanged();
                } else if (uploadPhotoList.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(uploadPhotoList.get(position).getFileName());
                    }
                }
                if (adapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }

            }
        });
        if (adapter.getItemCount() != 0) {
            tv_photo_list.setVisibility(View.GONE);
        } else {
            tv_photo_list.setVisibility(View.VISIBLE);
        }

    }

    //整改后
    private void recyclerViewAdapter_later() {
        if (adapter_later == null) {
            adapter_later = new UploadPhotoAdapter(this, uploadPhotoList_later);
            recyclerView_later.setAdapter(adapter_later);
        } else {
            adapter_later.notifyDataSetChanged();
        }
        adapter_later.setOnItemOnClilcklisten(new UploadPhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (uploadPhotoList_later.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    uploadPhotoList_later.remove(position);
                    adapter_later.notifyDataSetChanged();
                } else if (uploadPhotoList_later.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(uploadPhotoList_later.get(position).getFileName());
                    }
                }
                if (adapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }

            }
        });
        if (adapter_later.getItemCount() != 0) {
            tv_photo_list_later.setVisibility(View.GONE);
        } else {
            tv_photo_list_later.setVisibility(View.VISIBLE);
        }

    }

    //视频整改前
    private void recyclerViewAdapter_video() {
        if (adapter_video == null) {
            adapter_video = new UploadVideoAdapter(this, videoList);
            recyclerView_video.setAdapter(adapter_video);
        } else {
            adapter_video.notifyDataSetChanged();
        }
        adapter_video.setOnItemOnClilcklisten(new UploadVideoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (videoList.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    videoList.remove(position);
                    adapter_video.notifyDataSetChanged();
                } else if (videoList.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(videoList.get(position).getFileName());
                    }
                }
                if (adapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }

            }
        });
        if (adapter_video.getItemCount() != 0) {
            tv_video_list.setVisibility(View.GONE);
        } else {
            tv_video_list.setVisibility(View.VISIBLE);
        }

    }

    //视频整改后
    private void recyclerViewAdapter_video_later() {
        if (adapter_video_later == null) {
            adapter_video_later = new UploadVideoAdapter(this, videoList_later);
            recyclerView_video_later.setAdapter(adapter_video_later);
        } else {
            adapter_video_later.notifyDataSetChanged();
        }
        adapter_video_later.setOnItemOnClilcklisten(new UploadVideoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemDeleteClick(View v, int position) {
                if (videoList_later.get(position).getIsSuccess() == 1) { //上传成功  显示删除
                    videoList_later.remove(position);
                    adapter_video_later.notifyDataSetChanged();
                } else if (videoList_later.get(position).getIsSuccess() == 0) {

                } else { //上传失败 重新上传
                    if (mPresenter != null) {
                        mPresenter.uploadFile(videoList_later.get(position).getFileName());
                    }
                }
                if (adapter.getItemCount() != 0) {
                    tv_photo_list.setVisibility(View.GONE);
                } else {
                    tv_photo_list.setVisibility(View.VISIBLE);
                }

            }
        });
        if (adapter_video_later.getItemCount() != 0) {
            tv_video_list_later.setVisibility(View.GONE);
        } else {
            tv_video_list_later.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void showLoading() {
        getDailog().showProgressDailog(this, null);
    }

    CommProgressDailog dialogUtils;

    private CommProgressDailog getDailog() {
        if (dialogUtils == null) {
            dialogUtils = new CommProgressDailog();
        }
        return dialogUtils;
    }

    @Override
    public void hideLoading() {
        getDailog().cancelProgressDailog();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        Toast.makeText(this, "上报案件成功", Toast.LENGTH_LONG).show();
        setResult(1);
        finish();
    }

    @OnClick({R.id.btn_image, R.id.btn_image_later, R.id.btn_video, R.id.btn_video_later, R.id.tv_ok, R.id.tv_previous_step, R.id.tv_cancel})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_image:
                isBeforePhoto = true;
                checkPermissionAndAction();
                break;
            case R.id.btn_image_later: //整改后
                isBeforePhoto = false;
                checkPermissionAndAction();
                break;
            case R.id.btn_video:
//                startVideo();
                intent = new Intent(this, VideoRecordActivity.class);
                startActivityForResult(intent, RESULT_VIDEO);
                break;
            case R.id.btn_video_later:
//                startVideo();
                intent = new Intent(this, VideoRecordActivity.class);
                startActivityForResult(intent, RESULT_VIDEO_later);
                break;
            case R.id.tv_ok:
                List<UploadCaseFile> caseFileList = new ArrayList<>();

                int handleType = 1;//直接处理传1 ，非直接处理传2
//                int whenType = 1;//直接处理( 整改前的写1  整改后写2),  非直接处理 whenType 1
                int caseProcessRecordID = 19;// 直接处理 caseProcessRecordID  19,  非直接处理 caseProcessRecordID  11
                if (entry_type == 0) {
                    //自行处理
                    handleType = 1;
                    caseProcessRecordID = 19;
                } else if (entry_type == 1) {
                    //非自行处理
                    handleType = 2;
                    caseProcessRecordID = 11;
                }

                if (uploadPhotoList != null) { //照片整改前
                    for (int i = 0; i < uploadPhotoList.size(); i++) {
                        UploadCaseFile caseFile = new UploadCaseFile();
                        caseFile.setCaseId(caseId);
                        caseFile.setUrl(uploadPhotoList.get(i).getFileRelativePath());
                        caseFile.setCaseProcessRecordId(caseProcessRecordID);
                        caseFile.setFileType(0); //照片
                        caseFile.setWhenType(1); //整改前
                        caseFile.setHandleType(handleType);
                        caseFileList.add(caseFile);
                    }
                }
                if (uploadPhotoList_later != null) { //照片整改前
                    for (int i = 0; i < uploadPhotoList_later.size(); i++) {
                        UploadCaseFile caseFile = new UploadCaseFile();
                        caseFile.setCaseId(caseId);
                        caseFile.setUrl(uploadPhotoList_later.get(i).getFileRelativePath());
                        caseFile.setCaseProcessRecordId(caseProcessRecordID);
                        caseFile.setFileType(0); //照片
                        caseFile.setWhenType(2);//整改后
                        caseFile.setHandleType(handleType);
                        caseFileList.add(caseFile);
                    }
                }
                if (videoList != null) { //照片整改前
                    for (int i = 0; i < videoList.size(); i++) {
                        UploadCaseFile caseFile = new UploadCaseFile();
                        caseFile.setCaseId(caseId);
                        caseFile.setUrl(videoList.get(i).getFileRelativePath());
                        caseFile.setCaseProcessRecordId(0);
                        caseFile.setFileType(1); //视频
                        caseFile.setWhenType(1);//整改后
                        caseFile.setHandleType(handleType);
                        caseFileList.add(caseFile);
                    }
                }
                if (videoList_later != null) { //照片整改前
                    for (int i = 0; i < videoList_later.size(); i++) {
                        UploadCaseFile caseFile = new UploadCaseFile();
                        caseFile.setCaseId(caseId);
                        caseFile.setUrl(videoList_later.get(i).getFileRelativePath());
                        caseFile.setCaseProcessRecordId(caseProcessRecordID);
                        caseFile.setFileType(1); //视频
                        caseFile.setWhenType(2);//整改后
                        caseFile.setHandleType(handleType);
                        caseFileList.add(caseFile);
                    }
                }
                if (mPresenter != null) {
                    mPresenter.addCaseAttach(caseFileList);
                }
                break;
            case R.id.tv_previous_step:
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    /**
     * 视频拍摄
     */
    private void startVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File file = new File(Environment.getExternalStorageDirectory(), "haha.3gp");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        //设置保存视频文件的质量
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, RESULT_VIDEO);
    }

    private void showDialog() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0: // 直接调起相机
                        //启动相机程序
                        openCamera();
                        break;
                    case 1:
//                        intent = new Intent(Intent.ACTION_PICK);
//                        intent.setType("image/*");
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            startActivityForResult(intent, REQUEST_CODE_ALBUM_PHOTO);
//                        } else {
//                            startActivityForResult(intent, REQUEST_CODE_ALBUM);
//                        }

                        intent = new Intent(UploadActivity.this, PhotoActivityActivity.class);
                        startActivityForResult(intent, RESULT_PHOTO);
                        break;
                    default:
                        break;
                }
            }
        }, names);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


    private Uri imageUri;

    private void openCamera() {
//        File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
                outputImage.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_ALBUM:  //4.4 以前
                if (data == null) return;

                if (data.getData() == null) return;

                String path = data.getData().getPath();
                compressImageUpload(path);
                break;
            case REQUEST_CODE_ALBUM_PHOTO: //4.4以后
                if (data == null) return;
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    String picturePath = PictureHelper.getPath(this, selectedImage);
                    compressImageUpload(picturePath);
                }
                break;
            case RESULT_CAMERA: //相机
                if (imageUri != null) {
                    compressImageUpload(imageUri.getPath());
                }

//                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                    img_.setImageBitmap(bitmap);
                break;
            case RESULT_PHOTO: //选择多张图片返回
                if (data != null && data.hasExtra("selectPhotoList")) {
                    List<UploadFile> selectPhotoList = data.getParcelableArrayListExtra("selectPhotoList");
                    if (selectPhotoList != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("图片上传");
                        builder.setMessage("上传将会产生流量");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
//                                mPresenter.uploadFileList(uploadPhotoList);
                                if (isBeforePhoto) {  //整改前
                                    compressImageUploadList(selectPhotoList, uploadPhotoList);
                                } else {//整改后
                                    compressImageUploadList(selectPhotoList, uploadPhotoList_later);
                                }
                            }
                        });
                        builder.show();

                    }
                }
            case RESULT_VIDEO: //视频录制
                if (data != null) {
//                   Uri uri =  data.getData();
//                   if(uri!=null && !TextUtils.isEmpty(uri.getPath())){
//                       mPresenter.uploadFile(uri.getPath());
//                   }

                    UploadFile uploadFile = data.getParcelableExtra("UploadFile");
                    if (uploadFile != null) {
                        String size = FileSizeUtil.getAutoFileOrFilesSize(uploadFile.getFileName());
                        uploadFile.setFileSize(size);
                        if (videoList == null) {
                            videoList = new ArrayList<>();
                        }
                        videoList.add(uploadFile);
                        recyclerViewAdapter_video();
                    }
                }
                break;
            case RESULT_VIDEO_later:
                if (data != null) {
                    UploadFile uploadFile = data.getParcelableExtra("UploadFile");
                    if (uploadFile != null) {
                        String size = FileSizeUtil.getAutoFileOrFilesSize(uploadFile.getFileName());
                        uploadFile.setFileSize(size);
                        if (videoList_later == null) {
                            videoList_later = new ArrayList<>();
                        }
                        videoList_later.add(uploadFile);
                        recyclerViewAdapter_video_later();
                    }
                }

                break;

        }
    }

    /**
     * 压缩单张图片并上传
     */
    private void compressImageUpload(String path) {
        if (!TextUtils.isEmpty(path)) {
            path = BitmapUtil.compressImage(path);
            if (!TextUtils.isEmpty(path)) {

                UploadFile uploadPhoto = new UploadFile();
                uploadPhoto.setFileName(imageUri.getPath());
                String size = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto.getFileName());
                uploadPhoto.setFileSize(size);
                if (isBeforePhoto) {  //整改前
                    if (uploadPhotoList == null) {
                        uploadPhotoList = new ArrayList<>();
                    }
                    uploadPhotoList.add(uploadPhoto);
                    recyclerViewAdapter();
                } else {//整改后
                    if (uploadPhotoList_later == null) {
                        uploadPhotoList_later = new ArrayList<>();
                    }
                    uploadPhotoList_later.add(uploadPhoto);
                    recyclerViewAdapter_later();
                }

                if (mPresenter != null) {
                    mPresenter.uploadFile(path);
                }
            }
        }
    }

    /**
     * 压缩多张图片并上传
     */
    private void compressImageUploadList(List<UploadFile> selectPhotoList, List<UploadFile> uploadList) {
        if (uploadList == null || uploadList.size() == 0) {
            uploadList = selectPhotoList;
            for (int i = 0; i < uploadList.size(); i++) {
                BitmapUtil.compressImage(uploadList.get(i).getFileName()); //压缩
                String size = FileSizeUtil.getAutoFileOrFilesSize(uploadList.get(i).getFileName());
                uploadList.get(i).setFileSize(size);
            }
        } else {
            for (int i = 0; i < selectPhotoList.size(); i++) {
                boolean flag = true;
                for (int j = 0; j < uploadList.size(); j++) {
                    if (selectPhotoList.get(i).getFileName().equals(uploadList.get(j).getFileName())) {  //同一张图片
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    UploadFile uploadPhoto = selectPhotoList.get(i);
                    BitmapUtil.compressImage(selectPhotoList.get(i).getFileName()); //压缩
                    String size = FileSizeUtil.getAutoFileOrFilesSize(uploadPhoto.getFileName());
                    uploadPhoto.setFileSize(size);
                    uploadList.add(uploadPhoto);
                }
            }
        }
        if (isBeforePhoto) {  //整改前
            uploadPhotoList = uploadList;
            recyclerViewAdapter();
        } else {//整改后
            uploadPhotoList_later = uploadList;
            recyclerViewAdapter_later();
        }

        for (int i = 0; i < uploadList.size(); i++) {
            if (!TextUtils.isEmpty(uploadList.get(i).getFileName())) {
//                uploadPhotoList.get(i).setFileName(BitmapUtil.compressImage(uploadPhotoList.get(i).getFileName()));
                if (mPresenter != null) {
                    mPresenter.uploadFile(uploadList.get(i).getFileName());
                }
//                mPresenter.uploadFileList(uploadPhotoList);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void checkPermissionAndAction() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            showDialog();
//                            showSelectDialog();
//                            Intent intent = new Intent(UploadActivity.this,PhotoActivityActivity.class);
//                            startActivityForResult(intent,RESULT_PHOTO);

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
//                            Log.d(TAG, permission.name + " is denied.");
                            showPermissionsDialog();

                        }
                    }
                });
    }

    /**
     * 提示需要权限 AlertDialog
     */
    private void showPermissionsDialog() {
        /*
         * 这里使用了 android.support.v7.app.AlertDialog.Builder
         * 可以直接在头部写 import android.support.v7.app.AlertDialog
         * 那么下面就可以写成 AlertDialog.Builder
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限提醒");
        builder.setMessage("上传图片需要文件权限");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionUtils.permissionSkipSetting(UploadActivity.this);
            }
        });
        builder.show();
    }

    @Override
    public void uploadSuccess(UploadFile uploadPhoto) {
        if (uploadPhoto != null) {
            if (isBeforePhoto) {
                for (int i = 0; i < uploadPhotoList.size(); i++) {
                    if (uploadPhotoList.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                        uploadPhotoList.get(i).setFileDomain(uploadPhoto.getFileDomain());
                        uploadPhotoList.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                        uploadPhotoList.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                        adapter.notifyItemChanged(i);
                    }
                }
            } else {
                for (int i = 0; i < uploadPhotoList_later.size(); i++) {
                    if (uploadPhotoList_later.get(i).getFileName().equals(uploadPhoto.getFileName())) {
                        uploadPhotoList_later.get(i).setFileDomain(uploadPhoto.getFileDomain());
                        uploadPhotoList_later.get(i).setFileRelativePath(uploadPhoto.getFileRelativePath());
                        uploadPhotoList_later.get(i).setIsSuccess(uploadPhoto.getIsSuccess());
                        adapter_later.notifyItemChanged(i);
                    }
                }
            }
        }

    }
}
