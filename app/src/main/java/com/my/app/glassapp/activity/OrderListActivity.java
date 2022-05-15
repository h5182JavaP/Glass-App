package com.my.app.glassapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.my.app.glassapp.PathUtil;
import com.my.app.glassapp.R;
import com.my.app.glassapp.utils.BtnOnClickListener;
import com.my.app.glassapp.utils.DataConverter;
import com.my.app.glassapp.adapter.AnnealedTableAdapter;
import com.my.app.glassapp.adapter.DGUTableAdapter;
import com.my.app.glassapp.adapter.LaminatedDGUTableAdapter;
import com.my.app.glassapp.adapter.LaminationTableAdapter;
import com.my.app.glassapp.adapter.SGUTableAdapter;
import com.my.app.glassapp.database.DBDao;
import com.my.app.glassapp.database.DBRoom;
import com.my.app.glassapp.databinding.ActivityOrderListBinding;
import com.my.app.glassapp.model.AnnealedTable;
import com.my.app.glassapp.model.DGUTable;
import com.my.app.glassapp.model.LaminatedDGUTable;
import com.my.app.glassapp.model.LaminationTable;
import com.my.app.glassapp.model.SGUTable;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

public class OrderListActivity extends AppCompatActivity implements BtnOnClickListener {

    ActivityOrderListBinding binding;
    private List<SGUTable> sguData;
    private List<DGUTable> dguData;
    private List<LaminationTable> laminationData;
    private List<AnnealedTable> annealedData;
    private List<LaminatedDGUTable> lDGUData;
    private DBDao dao;
    private SGUTableAdapter mAdapterSgu;
    private DGUTableAdapter mAdapterDgu;
    private LaminationTableAdapter mAdapterLamination;
    private AnnealedTableAdapter mAdapterAnnealed;
    private LaminatedDGUTableAdapter mAdapterLDgu;
    public static final int SELECT_PICTURE = 111;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private Bitmap bmpImage;
    private int rowPosition;
    private String tableValue;
    private boolean isPermitted;
    private String imgepath;
    String[] gapCategories = {"06", "08", "10", "12", "14", "15", "16", "18", "20", "22"};
    String[] pvbCategories = {"0.38", "0.16", "1.14", "1.52"};
    String dguGap;
    String lDguGap;
    String laminationPVB;
    String laminatedPVB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dao = DBRoom.getInstance(this).dao();

        binding.ivAddOrder.setOnClickListener(view -> {
            checkRunTimePermission();
        });

        sguData = dao.getSGUData();
        dguData = dao.getDGUData();
        laminationData = dao.getLaminationData();
        annealedData = dao.getAnnealedData();
        lDGUData = dao.getLDGUData();
//        add10TestItems(sguData);
        if (sguData.size() != 0) {
            binding.tvHeaderSGU.setText(sguData.get(0).getSgu_thickness() + "  " + sguData.get(0).getSgu_materialDetails());
        }
        if (dguData.size() != 0) {
            if (dguData.get(0).getDgu_gap().equals("0")) {
                dguGap = gapCategories[0];
            } else if (dguData.get(0).getDgu_gap().equals("1")) {
                dguGap = gapCategories[1];
            } else if (dguData.get(0).getDgu_gap().equals("2")) {
                dguGap = gapCategories[2];
            } else if (dguData.get(0).getDgu_gap().equals("3")) {
                dguGap = gapCategories[3];
            } else if (dguData.get(0).getDgu_gap().equals("4")) {
                dguGap = gapCategories[4];
            } else if (dguData.get(0).getDgu_gap().equals("5")) {
                dguGap = gapCategories[5];
            } else if (dguData.get(0).getDgu_gap().equals("6")) {
                dguGap = gapCategories[6];
            } else if (dguData.get(0).getDgu_gap().equals("7")) {
                dguGap = gapCategories[7];
            } else if (dguData.get(0).getDgu_gap().equals("8")) {
                dguGap = gapCategories[8];
            } else if (dguData.get(0).getDgu_gap().equals("9")) {
                dguGap = gapCategories[9];
            }
            binding.tvHeaderDGU.setText(dguData.get(0).getDgu_glass_1() + "   " + dguGap + "   " + dguData.get(0).getDgu_glass_2());
        }
        if (laminationData.size() != 0) {
            if (laminationData.get(0).getLamination_pvb().equals("0")) {
                laminationPVB = pvbCategories[0];
            } else if (laminationData.get(0).getLamination_pvb().equals("1")) {
                laminationPVB = pvbCategories[1];
            } else if (laminationData.get(0).getLamination_pvb().equals("2")) {
                laminationPVB = pvbCategories[2];
            } else if (laminationData.get(0).getLamination_pvb().equals("3")) {
                laminationPVB = pvbCategories[3];
            }
            binding.tvHeaderLamination.setText(laminationData.get(0).getLamination_glass_1() + "   " + laminationPVB + "   " + laminationData.get(0).getLamination_glass_2());
        }

        if (annealedData.size() != 0) {
            binding.tvHeaderAnnealed.setText(annealedData.get(0).getAnnealed_thickness() + "  " + annealedData.get(0).getAnnealed_materialDetails());
        }

        if (lDGUData.size() != 0) {
            if (lDGUData.get(0).getLdgu_gap().equals("0")) {
                lDguGap = gapCategories[0];
            } else if (lDGUData.get(0).getLdgu_gap().equals("1")) {
                lDguGap = gapCategories[1];
            } else if (lDGUData.get(0).getLdgu_gap().equals("2")) {
                lDguGap = gapCategories[2];
            } else if (lDGUData.get(0).getLdgu_gap().equals("3")) {
                lDguGap = gapCategories[3];
            } else if (lDGUData.get(0).getLdgu_gap().equals("4")) {
                lDguGap = gapCategories[4];
            } else if (lDGUData.get(0).getLdgu_gap().equals("5")) {
                lDguGap = gapCategories[5];
            } else if (lDGUData.get(0).getLdgu_gap().equals("6")) {
                lDguGap = gapCategories[6];
            } else if (lDGUData.get(0).getLdgu_gap().equals("7")) {
                lDguGap = gapCategories[7];
            } else if (lDGUData.get(0).getLdgu_gap().equals("8")) {
                lDguGap = gapCategories[8];
            } else if (lDGUData.get(0).getLdgu_gap().equals("9")) {
                lDguGap = gapCategories[9];
            }

            if (lDGUData.get(0).getLdgu_pvb().equals("0")) {
                laminatedPVB = pvbCategories[0];
            } else if (lDGUData.get(0).getLdgu_pvb().equals("1")) {
                laminatedPVB = pvbCategories[1];
            } else if (lDGUData.get(0).getLdgu_pvb().equals("2")) {
                laminatedPVB = pvbCategories[2];
            } else if (lDGUData.get(0).getLdgu_pvb().equals("3")) {
                laminatedPVB = pvbCategories[3];
            }
            binding.tvHeaderLaminatedDGU.setText(lDGUData.get(0).getLdgu_glass_1() + "   " + laminatedPVB + "  " + lDGUData.get(0).getLdgu_glass_2() + "  " + lDguGap + "   " + lDGUData.get(0).getLdgu_glass_3());
        }

        binding.rvSguTable.setHasFixedSize(true);
        binding.rvDguTable.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvSguTable.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        binding.rvDguTable.setLayoutManager(layoutManager1);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        binding.rvLaminationTable.setLayoutManager(layoutManager2);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this);
        binding.rvAnnealedTable.setLayoutManager(layoutManager3);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this);
        binding.rvLaminatedDguTable.setLayoutManager(layoutManager4);

        binding.cvAddSGU.setOnClickListener(view -> {
            Intent intent = new Intent(this, InquiryFormEditActivity.class);
            intent.putExtra("GlassType", "SGU");
            intent.putExtra("TablePosition", 0);
            intent.putExtra("AddTypeFlag", true);
            intent.putExtra("AddTypeFlag1", true);

            startActivity(intent);
            finish();
        });
        binding.cvAddDGU.setOnClickListener(view -> {
            Intent intent = new Intent(this, InquiryFormEditActivity.class);
            intent.putExtra("GlassType", "DGU");
            intent.putExtra("TablePosition", 0);
            intent.putExtra("AddTypeFlag", true);
            intent.putExtra("AddTypeFlag1", true);

            startActivity(intent);
            finish();
        });

        binding.cvAddLamination.setOnClickListener(view -> {
            Intent intent = new Intent(this, InquiryFormEditActivity.class);
            intent.putExtra("GlassType", "Lamination");
            intent.putExtra("TablePosition", 0);
            intent.putExtra("AddTypeFlag", true);
            intent.putExtra("AddTypeFlag1", true);

            startActivity(intent);
            finish();
        });
        binding.cvAddAnnealed.setOnClickListener(view -> {
            Intent intent = new Intent(this, InquiryFormEditActivity.class);
            intent.putExtra("GlassType", "Annealed");
            intent.putExtra("TablePosition", 0);
            intent.putExtra("AddTypeFlag", true);
            intent.putExtra("AddTypeFlag1", true);

            startActivity(intent);
            finish();
        });
        binding.cvAddLaminatedDGU.setOnClickListener(view -> {
            Intent intent = new Intent(this, InquiryFormEditActivity.class);
            intent.putExtra("GlassType", "LaminatedDGU");
            intent.putExtra("TablePosition", 0);
            intent.putExtra("AddTypeFlag", true);
            intent.putExtra("AddTypeFlag1", true);

            startActivity(intent);
            finish();
        });

        binding.cvOrder.setOnClickListener(view -> {
            startActivity(new Intent(OrderListActivity.this, AddressActivity.class));
            finish();
        });

        binding.imgLogout.setOnClickListener(view -> {

            final Dialog dialog = new Dialog(OrderListActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_logout);

            TextView tvYes = (TextView) dialog.findViewById(R.id.tv_yes);
            TextView tvNo = (TextView) dialog.findViewById(R.id.tv_no);

            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putBoolean("login", false);
                    myEdit.commit();
                    startActivity(new Intent(OrderListActivity.this, MainActivity.class));
                    finish();
                }
            });
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        });
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, PERMISSION_REQUEST_CODE);
        } else {
            startActivity(new Intent(this, InquiryFormActivity.class));
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (sguData.isEmpty()) {
            binding.tvSGUTitle.setVisibility(View.GONE);
            binding.tvHeaderSGU.setVisibility(View.GONE);
            binding.cvAddSGU.setVisibility(View.GONE);
            binding.hsvSGU.setVisibility(View.GONE);
        }
        if (dguData.isEmpty()) {
            binding.tvDGUTitle.setVisibility(View.GONE);
            binding.tvHeaderDGU.setVisibility(View.GONE);
            binding.cvAddDGU.setVisibility(View.GONE);
            binding.hsvDGU.setVisibility(View.GONE);
        }
        if (lDGUData.isEmpty()) {
            binding.tvLaminatedDGUTitle.setVisibility(View.GONE);
            binding.tvHeaderLaminatedDGU.setVisibility(View.GONE);
            binding.cvAddLaminatedDGU.setVisibility(View.GONE);
            binding.hsvLaminatedDGU.setVisibility(View.GONE);
        }
        if (laminationData.isEmpty()) {
            binding.tvLaminationTitle.setVisibility(View.GONE);
            binding.tvHeaderLamination.setVisibility(View.GONE);
            binding.cvAddLamination.setVisibility(View.GONE);
            binding.hsvLamination.setVisibility(View.GONE);
        }
        if (annealedData.isEmpty()) {
            binding.tvAnnealedTitle.setVisibility(View.GONE);
            binding.tvHeaderAnnealed.setVisibility(View.GONE);
            binding.cvAddAnnealed.setVisibility(View.GONE);
            binding.hsvAnnealed.setVisibility(View.GONE);
        }

        if (sguData.isEmpty() && dguData.isEmpty() && lDGUData.isEmpty() && laminationData.isEmpty() && annealedData.isEmpty()) {
            binding.cvOrder.setVisibility(View.GONE);
        }

        mAdapterSgu = new SGUTableAdapter(this, this, sguData, this);
        binding.rvSguTable.setAdapter(mAdapterSgu);

        mAdapterDgu = new DGUTableAdapter(this, this, dguData, this);
        binding.rvDguTable.setAdapter(mAdapterDgu);

        mAdapterLamination = new LaminationTableAdapter(this, this, laminationData, this);
        binding.rvLaminationTable.setAdapter(mAdapterLamination);

        mAdapterAnnealed = new AnnealedTableAdapter(this, this, annealedData, this);
        binding.rvAnnealedTable.setAdapter(mAdapterAnnealed);

        mAdapterLDgu = new LaminatedDGUTableAdapter(this, this, lDGUData, this);
        binding.rvLaminatedDguTable.setAdapter(mAdapterLDgu);

//        binding.tvHeaderSGU.setLayoutParams(new LinearLayout.LayoutParams(binding.rvSguTable.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

        if (mAdapterSgu != null) {
            mAdapterSgu.notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri uri = data.getData();
//                imgepath = uri.getPath();
                try {
                    imgepath = PathUtil.getPath(this, uri);
//                    imgepath = uri.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    bmpImage = getBitmapFromUri(uri);
                    if (bmpImage != null) {
                        if (tableValue == "SGU") {
                            dao.updateSGUImage(rowPosition, DataConverter.convertImage2ByteArray(bmpImage), imgepath);
                            if (mAdapterSgu != null) {
                                mAdapterSgu.notifyDataSetChanged();
                            }
                        } else if (tableValue == "DGU") {
                            dao.updateDGUImage(rowPosition, DataConverter.convertImage2ByteArray(bmpImage), imgepath);
                            if (mAdapterDgu != null) {
                                mAdapterDgu.notifyDataSetChanged();
                            }
                        } else if (tableValue == "Lamination") {
                            dao.updateLaminationImage(rowPosition, DataConverter.convertImage2ByteArray(bmpImage), imgepath);
                            if (mAdapterLamination != null) {
                                mAdapterLamination.notifyDataSetChanged();
                            }
                        } else if (tableValue == "Annealed") {
                            dao.updateAnnealedImage(rowPosition, DataConverter.convertImage2ByteArray(bmpImage), imgepath);
                            if (mAdapterAnnealed != null) {
                                mAdapterAnnealed.notifyDataSetChanged();
                            }
                        } else if (tableValue == "LaminatedDGU") {
                            dao.updateLDGUImage(rowPosition, DataConverter.convertImage2ByteArray(bmpImage), imgepath);
                            if (mAdapterLDgu != null) {
                                mAdapterLDgu.notifyDataSetChanged();
                            }
                        }
                        recreate();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            try {
                imgepath = PathUtil.getPath(this, uri);
//                imgepath = uri.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (photo != null) {
                    if (tableValue == "SGU") {
                        dao.updateSGUImage(rowPosition, DataConverter.convertImage2ByteArray(photo), imgepath);
                        if (mAdapterSgu != null) {
                            mAdapterSgu.notifyDataSetChanged();
                        }
                    } else if (tableValue == "DGU") {
                        dao.updateDGUImage(rowPosition, DataConverter.convertImage2ByteArray(photo), imgepath);
                        if (mAdapterDgu != null) {
                            mAdapterDgu.notifyDataSetChanged();
                        }
                    } else if (tableValue == "Lamination") {
                        dao.updateLaminationImage(rowPosition, DataConverter.convertImage2ByteArray(photo), imgepath);
                        if (mAdapterLamination != null) {
                            mAdapterLamination.notifyDataSetChanged();
                        }
                    } else if (tableValue == "Annealed") {
                        dao.updateAnnealedImage(rowPosition, DataConverter.convertImage2ByteArray(photo), imgepath);
                        if (mAdapterAnnealed != null) {
                            mAdapterAnnealed.notifyDataSetChanged();
                        }
                    } else if (tableValue == "LaminatedDGU") {
                        dao.updateLDGUImage(rowPosition, DataConverter.convertImage2ByteArray(photo), imgepath);
                        if (mAdapterLDgu != null) {
                            mAdapterLDgu.notifyDataSetChanged();
                        }
                    }
                    recreate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                    }
                }
            }

            if (isPermitted) {
                startActivity(new Intent(this, InquiryFormActivity.class));
                finish();
            }
        }

    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    @Override
    public void onClickCamera(int position, String value) {
        rowPosition = position;
        tableValue = value;
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onClickGallery(int position, String value) {
        rowPosition = position;
        tableValue = value;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
}