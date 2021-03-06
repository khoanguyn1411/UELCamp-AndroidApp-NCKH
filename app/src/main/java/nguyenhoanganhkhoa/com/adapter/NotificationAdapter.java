package nguyenhoanganhkhoa.com.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nguyenhoanganhkhoa.com.custom.dialog.CustomDialogNotify;
import nguyenhoanganhkhoa.com.models.Notification;
import nguyenhoanganhkhoa.com.myapplication.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Notification> mNotification;
    private List<Notification> mNotificationOld;


    private int layoutItem;

    public NotificationAdapter(Context context, int layoutItem, int screen) {
        this.context = context;
        this.layoutItem = layoutItem;
        this.screen = screen;
    }

    private int screen = 0;

    //Friends
    public static final String NOTIFICATION_FRIEND = "friend";
    public static final String NOTIFICATION_GENDER_MALE = "male";
    public static final String NOTIFICATION_GENDER_FEMALE = "female";

    //Transaction
    public static final String NOTIFICATION_WALLET = "wallet";
    public static final String NOTIFICATION_TRANSFER= "transfer";

    //From services
    public static final String NOTIFICATION_PARKING = "parking";
    public static final String NOTIFICATION_QUANCAFE = "quancafe";
    public static final String NOTIFICATION_THUQUAN = "thuquan";
    public static final String NOTIFICATION_CANTEEN = "canteen";

    //Promotion
    public static final String NOTIFICATION_PROMOTION = "promotion";




    public NotificationAdapter(Context context, int layoutItem) {

        this.context = context;
        this.layoutItem = layoutItem;
    }

    public void setData(List<Notification> list){
        this.mNotification = list;
        if(layoutItem == R.layout.item_notification_all_bold){
            this.mNotificationOld = list;
        }
        notifyDataSetChanged();
    }


    private static int TYPE_FRIEND_REQUEST = 1;
    private static int TYPE_NORMAL = 2;
    @Override
    public int getItemViewType(int position) {
        Notification notification = mNotification.get(position);
        if(notification.isFriendRequest()){
            return TYPE_FRIEND_REQUEST;
        }
        else{
            return TYPE_NORMAL;
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType == TYPE_FRIEND_REQUEST && screen !=1){
            view = inflater.inflate(R.layout.item_notification_friend_request,parent,false);
        }
        else{
            view =  inflater.inflate(layoutItem,parent,false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = mNotification.get(position);
        if(notification ==null)
        {
            return;
        }
        holder.txtNotificationDate.setText(notification.getNotificationDate());
        holder.txtNotificationContent.setText(notification.getNotificationContent());
        switch (notification.getNotificationType()){
            case NOTIFICATION_FRIEND:
                if(notification.getGender().equals(NOTIFICATION_GENDER_MALE)){
                    holder.imvThumbNotification.setImageResource(R.drawable.img_avatar_male);
                }
                else{
                    holder.imvThumbNotification.setImageResource(R.drawable.img_avatar_female);
                }
                break;
            case NOTIFICATION_WALLET:
                setNotificationThumb(holder, R.drawable.ic_topup);
                break;
            case NOTIFICATION_TRANSFER:
                setNotificationThumb(holder, R.drawable.ic_transfer);
                break;
            case NOTIFICATION_QUANCAFE:
                setNotificationThumb(holder, R.drawable.ic_quancafe);
                break;
            case NOTIFICATION_THUQUAN:
                setNotificationThumb(holder, R.drawable.ic_thuquan);
                break;
            case NOTIFICATION_CANTEEN:
                setNotificationThumb(holder, R.drawable.ic_canteen);
                break;
            case NOTIFICATION_PARKING:
                setNotificationThumb(holder, R.drawable.ic_bike);
                break;
            case NOTIFICATION_PROMOTION:
                setNotificationThumb(holder, R.drawable.ic_promotion);
                break;
        }
        setNotificationStatus(holder,notification);
        addEvents(holder, notification, position);

    }
    private void setNotificationStatus(NotificationAdapter.ViewHolder holder, Notification notification){
        if(notification.isNewNotification()){
            holder.viewNewsNotification.setVisibility(View.VISIBLE);
            Typeface typeface = context.getResources().getFont(R.font.be_vietnam_bold);
            holder.txtNotificationContent.setTypeface(typeface);
        }
        else{
            holder.viewNewsNotification.setVisibility(View.GONE);
            Typeface typeface = context.getResources().getFont(R.font.be_vietnam_light);
            holder.txtNotificationContent.setTypeface(typeface);
        }
    }
    private void setNotificationThumb(NotificationAdapter.ViewHolder holder, int thumb){
        holder.imvThumbNotification.setImageResource(thumb);
        if(layoutItem == R.layout.item_notification_all_bold){
            holder.imvThumbNotification.setPadding(25,25,25,25);
        }
        else{
            holder.imvThumbNotification.setPadding(20,20,20,20);
        }
        holder.imvThumbNotification.setBackground(context.getDrawable(R.drawable.oval_shape));
    }
    private void addEvents(NotificationAdapter.ViewHolder holder, Notification notification, int position){
        if(layoutItem==R.layout.item_notification_all_bold)
        {
            holder.layout_item_notification_all_bold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(notification.isNewNotification()){
                        notification.setNewNotification(false);
                        setNotificationStatus(holder, notification);
                    }
                    displayFullNotification(position);
                }
            });
            holder.imbMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context wrapper = new ContextThemeWrapper(context,R.style.PopUpCustomStyle);
                    PopupMenu popupMenu = new PopupMenu(wrapper,holder.imbMore);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_edit_notification,popupMenu.getMenu());

                    if(!notification.isNewNotification()){
                        popupMenu.getMenu().findItem(R.id.mnMarkAsRead).setTitle("Mark as unread");
                    }
                    else{
                        popupMenu.getMenu().findItem(R.id.mnMarkAsRead).setTitle("Mark as read");
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId()==R.id.mnMarkAsRead){
                                if(menuItem.getTitle()=="Mark as read"){
                                    notification.setNewNotification(false);
                                    setNotificationStatus(holder, notification);
                                }
                                else{
                                    notification.setNewNotification(true);
                                    setNotificationStatus(holder, notification);
                                }
                            }
                            if(menuItem.getItemId()==R.id.mnDelete){
                                deleteItem(position);
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

    }

    private void displayFullNotification(int position) {
        CustomDialogNotify dialog = new CustomDialogNotify(context,R.layout.custom_dialog_display_notify);
        dialog.txtText.setText(mNotification.get(position).getNotificationContent());
        dialog.txtDate.setText(mNotification.get(position).getNotificationDate());
        dialog.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void deleteItem (int position){
        try {
            mNotification.remove(position);
            notifyItemRemoved(position);
            // d??ng n??y ????? load l???i d??? li???u sau khi ???? x??a l??n
            notifyItemRangeChanged(position, mNotification.size());
        }
        catch (Exception e){
            Log.d("Error", "Fail to delete item in Notification Adapter: " + e);
            Toast.makeText(context, "Delete notification fail!", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public int getItemCount() {
        if(mNotification !=null)
            return mNotification.size();
        else
            return 0;
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if(layoutItem == R.layout.item_notification_all_bold){
                    String textSearch = charSequence.toString();
                    if(textSearch.isEmpty()){
                        mNotification = mNotificationOld;
                        callBack.changeFragment();
                    }
                    else{
                        List<Notification> list = new ArrayList<>();
                        for(Notification notification: mNotificationOld){
                            if(notification.getNotificationContent().toLowerCase()
                                    .contains(textSearch.toLowerCase().trim())){
                                list.add(notification);
                            }
                        }
                        mNotification = list;
                        if(mNotification.size()==0){
                            callBack.changeToNoResultFragment();
                        }
                    }


                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mNotification;
                    return filterResults;

                }
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(layoutItem ==R.layout.item_notification_all_bold){
                    mNotification = (List<Notification>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    NotificationAdapter.MyCallBack callBack;
    public interface MyCallBack {
        void changeFragment();
        void changeToNoResultFragment();

    }
    public void setCallBack(NotificationAdapter.MyCallBack callBack) {
        this.callBack = callBack;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNotificationDate,txtNotificationContent;
        ImageView imvThumbNotification;
        View viewNewsNotification;

        ConstraintLayout layout_item_notification,layout_item_notification_all_bold;
        ImageButton imbMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNewsNotification = itemView.findViewById(R.id.viewNewsNotification);

            txtNotificationDate = itemView.findViewById(R.id.txtNotificationDate);
            txtNotificationContent = itemView.findViewById(R.id.txtNotificationContent);
            imvThumbNotification = itemView.findViewById(R.id.imvThumbNotification);
            layout_item_notification = itemView.findViewById(R.id.layout_item_notification);
            layout_item_notification_all_bold = itemView.findViewById(R.id.layout_item_notification_all_bold);
            imbMore = itemView.findViewById(R.id.imbMore);
        }
    }
}
