package com.my.app.glassapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.my.app.glassapp.utils.BtnOnClickListener;
import com.my.app.glassapp.utils.DataConverter;
import com.my.app.glassapp.R;
import com.my.app.glassapp.activity.InquiryFormEditActivity;
import com.my.app.glassapp.activity.ViewImageActivity;
import com.my.app.glassapp.model.LaminationTable;

import java.util.List;

public class LaminationTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER_ROW_TYPE = 0;
    public static final int REGULAR_ROW_TYPE = 1;
//    public static final int TITLE_ROW_TYPE = 2;

    private Context context;
    private Bitmap bmpImage = null;
    Activity activity;
    private List<LaminationTable> paymentModelList;
    BtnOnClickListener listener;

    public LaminationTableAdapter(Context context, Activity activity, List<LaminationTable> paymentModelList, BtnOnClickListener listener) {
        this.context = context;
        this.activity = activity;
        this.paymentModelList = paymentModelList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return HEADER_ROW_TYPE;
        } /*else if (2 == position) {
            return TITLE_ROW_TYPE;
        }*/ else {
            return REGULAR_ROW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if (viewType == HEADER_ROW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item_header_row,
                    parent, false);
            viewHolder = new ViewHolderHeaderRow(view);
        } /*else if (viewType == TITLE_ROW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item_header_row,
                    parent, false);
            viewHolder = new ViewHolderTitle(view);
        } */ else {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item_regular_row,
                    parent, false);
            viewHolder = new ViewHolderRegularRow(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position == 0) {
            ViewHolderHeaderRow headerAdapter = (ViewHolderHeaderRow) holder;
        }/*else if (position == 2) {
            ViewHolderTitle titleAdapter = (ViewHolderTitle) holder;
        }*/ else {
            ViewHolderRegularRow regularAdapter = (ViewHolderRegularRow) holder;
            regularAdapter.setData(paymentModelList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return (paymentModelList.size() + 1);
    }

    public class ViewHolderRegularRow extends RecyclerView.ViewHolder {

        private TextView srNo;
        private TextView width;
        private TextView height;
        private TextView quantity;
        private TextView notes;
        private ImageView btnPhoto;
        private ImageView btnEdit;

        public ViewHolderRegularRow(@NonNull View itemView) {
            super(itemView);
            srNo = itemView.findViewById(R.id.tv_sr_no);
            width = itemView.findViewById(R.id.tv_width);
            height = itemView.findViewById(R.id.tv_height);
            quantity = itemView.findViewById(R.id.tv_quantity);
            notes = itemView.findViewById(R.id.tv_notes);
            btnPhoto = itemView.findViewById(R.id.btn_photo);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }

        public void setData(LaminationTable laminationTable) {
            srNo.setText(String.valueOf(laminationTable.getLamination_id()));
            width.setText(laminationTable.getLamination_glassWidth());
            height.setText(laminationTable.getLamination_glassHeight());
            quantity.setText(laminationTable.getLamination_quantity());
            notes.setText(laminationTable.getLamination_note());
         /*   if (laminationTable.getLamination_image() != null) {
                btnPhoto.setImageBitmap(DataConverter.convertByteArray2Image(laminationTable.getLamination_image()));
            }*/
            if (laminationTable.getLamination_image() != null) {
                    btnPhoto.setImageBitmap(DataConverter.convertByteArray2Image(laminationTable.getLamination_image()));
                    bmpImage = DataConverter.convertByteArray2Image(laminationTable.getLamination_image());
            }


            btnPhoto.setOnClickListener(view -> {
                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(R.layout.btm_sheet_dialog);
                dialog.setCanceledOnTouchOutside(true);

                CardView cvViewImage = (CardView) dialog.findViewById(R.id.cv_view_imag);
                CardView cvCamera = (CardView) dialog.findViewById(R.id.cv_camera);
                CardView cvGallery = (CardView) dialog.findViewById(R.id.cv_gallery);
                if (laminationTable.getLamination_image() == null) {
                    cvViewImage.setVisibility(View.GONE);
                }

                cvViewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bmpImage != null) {
                            Intent intent = new Intent(context, ViewImageActivity.class);
                            intent.putExtra("img", laminationTable.getLamination_image());
//                            intent.putExtra("img", DataConverter.convertImage2ByteArray(bmpImage));
                            context.startActivity(intent);
                        }

                        dialog.dismiss();
                    }
                });
                cvCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickCamera(getAdapterPosition(),"Lamination");
                        dialog.dismiss();
                    }
                });
                cvGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickGallery(getAdapterPosition(),"Lamination");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            });


            btnEdit.setOnClickListener(view -> {
                Toast.makeText(context, "" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, InquiryFormEditActivity.class);
                intent.putExtra("GlassType", "Lamination");
                intent.putExtra("TablePosition", laminationTable.getLamination_id());
                context.startActivity(intent);
                activity.finish();
            });

        }
    }


    public class ViewHolderHeaderRow extends RecyclerView.ViewHolder {

        public ViewHolderHeaderRow(@NonNull View itemView) {
            super(itemView);
        }

    } /*public class ViewHolderTitle extends RecyclerView.ViewHolder {

        public ViewHolderTitle(@NonNull View itemView) {
            super(itemView);
        }

    }*/


}