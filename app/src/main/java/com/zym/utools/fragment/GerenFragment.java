package com.zym.utools.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.zym.utools.R;
import com.zym.utools.entity.User;
import com.zym.utools.ui.LoginActivity;
import com.zym.utools.ui.WuliuActivity;
import com.zym.utools.utils.L;
import com.zym.utools.utils.SharedUtils;
import com.zym.utools.view.CustomDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GerenFragment extends Fragment implements View.OnClickListener {

    private EditText geren_fragment_name, geren_fragment_sex, geren_fragment_age, geren_fragment_desc;

    //修改/注销
    private Button button_ok, button_exit;

    //编辑资料 物流
    private TextView edit_user, wuliu;

    //圆形头像
    private CircleImageView profile_image;
    //Dialog
    private CustomDialog dialog;
    //Dialog按钮
    private Button dialog_camera, dialog_photo, dialog_cancel;
    //服务器头像地址
    private static String icon_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geren, container, false);
        initView(view);
        return view;
    }

    //初始化控件
    private void initView(View view) {
        //圆形头像
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        //Dialog
        dialog = new CustomDialog(getActivity(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //Dialog按钮
        dialog_camera = (Button) dialog.findViewById(R.id.dialog_camera);
        dialog_camera.setOnClickListener(this);
        dialog_photo = (Button) dialog.findViewById(R.id.dialog_photo);
        dialog_photo.setOnClickListener(this);
        dialog_cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        dialog_cancel.setOnClickListener(this);

        //EditText个人资料
        geren_fragment_name = (EditText) view.findViewById(R.id.geren_fragment_name);
        geren_fragment_sex = (EditText) view.findViewById(R.id.geren_fragment_sex);
        geren_fragment_age = (EditText) view.findViewById(R.id.geren_fragment_age);
        geren_fragment_desc = (EditText) view.findViewById(R.id.geren_fragment_desc);
        //默认不可输入
        setEnable(false);
        //设置内容
        User userInfo = BmobUser.getCurrentUser(User.class);
        geren_fragment_name.setText(userInfo.getUsername());
        geren_fragment_sex.setText(userInfo.isSex() ? "男" : "女");
        geren_fragment_age.setText(Integer.toString(userInfo.getAge()));
        geren_fragment_desc.setText(userInfo.getDesc());
        //从网络加载用户头像   异步任务
        icon_user = userInfo.getIcon_login();
        if (!icon_user.equals("")) {
            new Thread(thread).start();
        }
        //编辑按钮
        edit_user = (TextView) view.findViewById(R.id.geren_fragment_edit_user);
        edit_user.setOnClickListener(this);
        //编辑完成
        button_ok = (Button) view.findViewById(R.id.geren_fragment_button_ok);
        button_ok.setOnClickListener(this);
        //物流查询
        wuliu = (TextView) view.findViewById(R.id.geren_fragment_wuliu);
        wuliu.setOnClickListener(this);
        //退出按钮
        button_exit = (Button) view.findViewById(R.id.geren_fragment_exit_user);
        button_exit.setOnClickListener(this);
    }

    //控制输入框焦点
    private void setEnable(boolean state) {
        geren_fragment_name.setEnabled(state);
        geren_fragment_sex.setEnabled(state);
        geren_fragment_age.setEnabled(state);
        geren_fragment_desc.setEnabled(state);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //选择头像
            case R.id.profile_image:
                dialog.show();
                break;
            //Dialog按钮 拍照
            case R.id.dialog_camera:
                toCamera();
                break;
            //Dialog按钮 相册
            case R.id.dialog_photo:
                toPohoto();
                break;
            //Dialog按钮 取消
            case R.id.dialog_cancel:
                dialog.dismiss();
                break;
            //编辑资料
            case R.id.geren_fragment_edit_user:
                setEnable(true);
                button_ok.setVisibility(View.VISIBLE);
                break;
            //编辑完成
            case R.id.geren_fragment_button_ok:
                //拿到输入框的值
                String name = geren_fragment_name.getText().toString().trim();
                String sex = geren_fragment_sex.getText().toString().trim();
                String age = geren_fragment_age.getText().toString().trim();
                String desc = geren_fragment_desc.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(sex) & !TextUtils.isEmpty(age)) {
                    //更新
                    User user = new User();
                    user.setUsername(name);
                    user.setAge(Integer.parseInt(age));
                    //性别
                    if (sex.equals("男")) {
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }
                    //简介
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc("这个家伙很懒，什么都没有写");
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //修改成功
                                setEnable(false);
                                button_ok.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), R.string.toast_message2, Toast.LENGTH_SHORT).show();
                }
                break;
            //物流查询
            case R.id.geren_fragment_wuliu:
                startActivity(new Intent(getActivity(), WuliuActivity.class));
                break;
            //注销
            case R.id.geren_fragment_exit_user:
                //清除缓存用户对象
                User.logOut();
                //当前userState为null
                BmobUser userState = User.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    public static final String IMAGE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int PHOTO_REQUEST_CODE = 101;
    public static final int CROP_REQUEST_CODE = 102;
    public File file = null;

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPohoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相机
                case CAMERA_REQUEST_CODE:
                    file = new File(Environment.getExternalStorageDirectory(), IMAGE_NAME);
                    cutImage(Uri.fromFile(file));
                    break;
                //相册
                case PHOTO_REQUEST_CODE:
                    L.i(String.valueOf(data.getData()));
                    cutImage(data.getData());
                    break;
                //裁剪
                case CROP_REQUEST_CODE:
                    //可能点击取消
                    if (data != null) {
                        //设置图片
                        setImageView(data);
                        //设置图片之后 删除原图
                        if (file != null) {
                            file.delete();
                        }
                    }
                    break;
            }
        }
    }


    //裁剪图片
    private void cutImage(Uri uri) {
        if (uri == null) {
            return;
        } else {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            //设置裁剪是否可用
            intent.putExtra("crop", "true");
            //设置比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置图片质量
            intent.putExtra("outputX", 320);
            intent.putExtra("outputY", 320);
            //发送数据
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CROP_REQUEST_CODE);
        }
    }

    //设置裁剪之后的图片
    private void setImageView(Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = bundle.getParcelable("data");
        //设置到CircleImageView
        profile_image.setImageBitmap(bitmap);
        //图片存到本地
        File file = imageToSD(bitmap);
        potFile(file);

    }

    //裁剪图片存到本地
    private File imageToSD(Bitmap bitmap) {
        File file = new File("sdcard/icon_user.png");
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //上传图片到Bmob文件服务器
    private void potFile(final File file) {
        L.i(String.valueOf(file.getPath()));
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    User user = new User();
                    user.setIcon_login(bmobFile.getFileUrl());
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "保存数据成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "保存数据失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //删除上传成功之后的原图
                    file.delete();
                } else {
                    Toast.makeText(getActivity(), "上传文件失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            if (!icon_user.equals("")) {
                geticonUser(icon_user);
            }
        }
    };

    //从网络获取头像
    private void geticonUser(String url) {
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            bufferedInputStream.close();
            inputStream.close();
            Message message = new Message();
            message.obj = bitmap;
            handler.sendMessage(message);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //处理主线程任务
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            profile_image.setImageBitmap(bitmap);
        }
    };
}
