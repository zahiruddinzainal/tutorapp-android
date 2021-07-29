package zvhir.dev.tutormy;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class LearnerRecyclerAdapter extends RecyclerView.Adapter<zvhir.dev.tutormy.LearnerRecyclerAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener mListener;

    public LearnerRecyclerAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.learner_recycler_adapter, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int positionAdmin) {

        holder.mWasap.setTag(positionAdmin); // set and store unique position of button

        Upload uploadCurrent = mUploads.get(positionAdmin);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textViewHarga.setText(uploadCurrent.getHarga() + " / hour"); //nak retrieve harga dari database
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.default_placeholder)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewName;
        public TextView textViewHarga;
        public ImageView imageView;
        public Button mWasap;
        public ImageViewHolder(View itemView) {
            super(itemView);

            mWasap = itemView.findViewById(R.id.whatsappButton);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewHarga = itemView.findViewById(R.id.text_view_harga);
            imageView = itemView.findViewById(R.id.image_view_upload);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int positionAdmin = getAdapterPosition();
                if (positionAdmin != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(positionAdmin);
                }
            }
        }



    }

    public interface OnItemClickListener {
        void onItemClick(int positionAdmin);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }


}