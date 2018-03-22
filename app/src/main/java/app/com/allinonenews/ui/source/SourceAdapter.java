package app.com.allinonenews.ui.source;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

import app.com.allinonenews.R;
import app.com.allinonenews.model.SelectedSource;
import app.com.allinonenews.model.Source;
import app.com.allinonenews.util.PrefUtil;

/**
 * Created by mukesh on 5/4/17.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceHolder>{

    private final Context context;

    public interface UpdateTitleListener {
        void updateTitle(String title);
    }

    private List<Source> mainList=new ArrayList<>();
    private List<Source> showingList=new ArrayList<>();
    private SelectedSource selectedSource;
    private UpdateTitleListener updateTitleListener;


    public SourceAdapter(Context context,UpdateTitleListener updateTitleListener){
        selectedSource= PrefUtil.getInstance(context).getSelectedSource();
        this.context=context;
        this.updateTitleListener = updateTitleListener;
    }

    public void showMainList(List<Source> list){
        mainList.clear();
        mainList.addAll(list);
        showingList.clear();
        showingList.addAll(list);
        notifyDataSetChanged();
    }

    public void applyFilter(String country,String language,String category){
        List<Source> filtered=new ArrayList<>();

        if (!Strings.isNullOrEmpty(country)){
            for (Source so : mainList) {
                if (so.getCountry().equalsIgnoreCase(category))
                    filtered.add(so);
            }
        }
        if (!Strings.isNullOrEmpty(language)){
            for (Source so : mainList) {
                if (so.getLanguage().equalsIgnoreCase(language))
                    filtered.add(so);
            }
        }
        if (!Strings.isNullOrEmpty(category)){
            for (Source so : mainList) {
                if (so.getCategory().equalsIgnoreCase(category))
                    filtered.add(so);
            }
        }

        showingList.clear();
        if (filtered.size()>0) {
            showingList.addAll(filtered);
        }
        else {
            showingList.addAll(mainList);
        }

        String sel=getSelectedSourceNameIfListContain();
        updateTitleListener.updateTitle(sel);

        notifyDataSetChanged();


    }

    private String getSelectedSourceNameIfListContain() {
        if (showingList==null||showingList.size()==0)
            return context.getString(R.string.select_source);
        for (int i=0;i<showingList.size();i++){
            if (showingList.get(i).isSelected())
                return showingList.get(i).getName();
        }
        return context.getString(R.string.select_source);
    }

    public SelectedSource getSelectedSource() {
        return selectedSource;
    }

    @Override
    public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SourceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.source_item,parent,false));
    }

    @Override
    public void onBindViewHolder(SourceHolder holder, int position) {
            holder.bindData(showingList.get(position));
    }

    @Override
    public int getItemCount() {
        return showingList.size();
    }

    public class SourceHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout linearLayout;
        private ImageView sourceImage;
        private TextView name;
        private TextView description;
        private RadioButton radio;

        public SourceHolder(View itemView) {
            super(itemView);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.sourceItemParent);
            sourceImage=(ImageView)itemView.findViewById(R.id.sourceItemImage);
            name=(TextView)itemView.findViewById(R.id.sourceItemName);
            description=(TextView)itemView.findViewById(R.id.sourceItemDescription);
            radio=(RadioButton)itemView.findViewById(R.id.sourceItemCheck);
           // radio.setOnClickListener(this);
            linearLayout.setOnClickListener(this);

        }

        public void bindData(Source source) {
            name.setText(source.getName());
            description.setText(source.getDescription());
            radio.setChecked(source.isSelected());

            try {
                Glide.with(this.sourceImage.getContext()).load(source.getUrlsToLogos().getSmall())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                sourceImage.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                sourceImage.setVisibility(View.VISIBLE);
                                sourceImage.setImageDrawable(resource);
                                return false;
                            }
                        })
                        .into(this.sourceImage);
            }
            catch (Exception e){
                e.printStackTrace();
                sourceImage.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.sourceItemParent:
                   selectTheSource();
                    updateTitleListener.updateTitle(selectedSource.getSourceName());
                    //radio.setChecked(true);
                    break;
//                case R.id.sourceItemCheck:
//                    radio.setChecked(true);
//
//                    break;
            }
        }

        private void selectTheSource() {
            for (Source so : showingList) {
                so.setSelected(false);
            }

            for (Source so : mainList) {
                so.setSelected(false);
            }

            Source source=showingList.get(getAdapterPosition());
            source.setSelected(true);
            int index=mainList.indexOf(source);
            if (index >= 0 && index < mainList.size()) {
                mainList.get(index).setSelected(true);
            }

            selectedSource.setSourceId(source.getId());
            selectedSource.setSourceName(source.getName());
            notifyDataSetChanged();
        }
    }
}
