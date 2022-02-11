package nguyenhoanganhkhoa.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import nguyenhoanganhkhoa.com.models.Drink;
import nguyenhoanganhkhoa.com.models.DrinkInCart;
import nguyenhoanganhkhoa.com.myapplication.R;
import nguyenhoanganhkhoa.com.myapplication.home.quancafe.OrderDetailSLSpaceScreen;
import nguyenhoanganhkhoa.com.thirdlink.AppUtil;
import nguyenhoanganhkhoa.com.thirdlink.ReusedConstraint;

public class DrinkIncartAdapter extends RecyclerView.Adapter<DrinkIncartAdapter.ViewHolder> {

    private Context context;
    private List<DrinkInCart> mDrinks;
    private int numScreen = 0;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    ReusedConstraint reusedConstraint = new ReusedConstraint(context);

    public DrinkIncartAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DrinkInCart> list){
        this.mDrinks = list;
        notifyDataSetChanged();
    }

    public void setNumScreen(int numScreen) {
        this.numScreen = numScreen;
    }

    public List<DrinkInCart> getmDrinks() {
        return mDrinks;
    }

    @NonNull
    @Override
    public DrinkIncartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(numScreen!=0){
            view = inflater.inflate(R.layout.item_drink_order,parent,false);
        }
        else {
             view = inflater.inflate(R.layout.item_drink_in_cart,parent,false);

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkIncartAdapter.ViewHolder holder, int position) {
        DrinkInCart drink = mDrinks.get(position);
        if(drink==null)
        {
            return;
        }


        viewBinderHelper.bind(holder.swipeRevealLayout,drink.getDrinkName());
        viewBinderHelper.setOpenOnlyOne(true);
        holder.txtNameDrink.setText(drink.getDrinkName());
        holder.txtIceLevel.setText(drink.toIcePercentString());
        holder.txtSugarLevel.setText(drink.toSugarPercentString());
        holder.txtSize.setText(drink.getSize());
        holder.txtPriceDrink.setText(reusedConstraint.formatCurrency(drink.getTotalPrice()));
        holder.imvThumbDrink.setImageResource(drink.getThumbDrink());
        holder.txtQuantity.setText(String.valueOf(drink.getQuantityDrink()));

        holder.imbPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drink.setQuantityDrink(drink.getQuantityDrink() + 1);
                holder.txtQuantity.setText(String.valueOf(drink.getQuantityDrink()));
                holder.txtPriceDrink.setText(reusedConstraint.formatCurrency(drink.getTotalPrice()));

                callBack.setTextPriceTotal(mDrinks);

            }
        });

        holder.imbMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drink.getQuantityDrink()>0){
                    drink.setQuantityDrink(drink.getQuantityDrink() - 1);
                    holder.txtQuantity.setText(String.valueOf(drink.getQuantityDrink()));
                    holder.txtPriceDrink.setText(reusedConstraint.formatCurrency(drink.getTotalPrice()));

                    callBack.setTextPriceTotal(mDrinks);

                    // Có thể dùng notifyDataSetChanged
                }
                if(drink.getQuantityDrink() == 0){
                    deleteItem(holder,drink);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(holder,drink);
            }
        });

        if(numScreen==0){
            setSelectedStatus(holder,drink);
            holder.imvSelectedItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(drink.isSelected()){
                        drink.setSelected(false);
                    }
                    else{
                        drink.setSelected(true);
                    }
                    setSelectedStatus(holder,drink);
                }
            });
        }



    }



    private void deleteItem(DrinkIncartAdapter.ViewHolder holder, DrinkInCart drink){
        String itemName = drink.getDrinkName();
        mDrinks.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
        callBack.showSnackBar(itemName);
        callBack.getListSizeRemain(mDrinks.size());
        callBack.setTextPriceTotal(mDrinks);
    }


    private void setSelectedStatus(DrinkIncartAdapter.ViewHolder holder, DrinkInCart drink){
        if (drink.isSelected()){
            holder.imvSelectedItem.setImageResource(R.drawable.ic_rad_yellow_checked);
        }
        else{
            holder.imvSelectedItem.setImageResource(R.drawable.ic_rad_yellow_unchecked);
        }
    }


    DrinkIncartAdapter.MyCallBack callBack;
    public interface MyCallBack {
        void setTextPriceTotal(List<DrinkInCart> list);
        void showSnackBar(String itemName);
        void getListSizeRemain(int size);
    }
    public void setCallBack(DrinkIncartAdapter.MyCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int getItemCount() {
        if(mDrinks !=null)
            return mDrinks.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvSelectedItem;
        private ImageView imvThumbDrink, imbMinus, imbPlus;
        private TextView txtQuantity, txtNameDrink, txtIceLevel, txtSugarLevel, txtSize, txtPriceDrink;
        private Button btnEditDrink, btnDelete;
        private SwipeRevealLayout swipeRevealLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvSelectedItem = itemView.findViewById(R.id.imvSelectedItem);
            imvThumbDrink = itemView.findViewById(R.id.imvThumbDrink);
            imbMinus = itemView.findViewById(R.id.imbMinus);
            imbPlus = itemView.findViewById(R.id.imbPlus);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtNameDrink = itemView.findViewById(R.id.txtNameDrink);
            txtIceLevel = itemView.findViewById(R.id.txtIceLevel);
            txtSugarLevel = itemView.findViewById(R.id.txtSugarLevel);
            txtSize = itemView.findViewById(R.id.txtSize);
            txtPriceDrink = itemView.findViewById(R.id.txtPriceDrink);
            btnEditDrink = itemView.findViewById(R.id.btnEditDrink);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}