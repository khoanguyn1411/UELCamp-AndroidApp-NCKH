package nguyenhoanganhkhoa.com.myapplication.home.SLSpace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import nguyenhoanganhkhoa.com.adapter.DrinkIncartAdapter;
import nguyenhoanganhkhoa.com.adapter.PurchaseAdapter;
import nguyenhoanganhkhoa.com.custom.dialog.CustomDialogTwoButtonNew;
import nguyenhoanganhkhoa.com.models.DrinkInCart;
import nguyenhoanganhkhoa.com.models.PurchaseItem;
import nguyenhoanganhkhoa.com.myapplication.R;
import nguyenhoanganhkhoa.com.thirdlink.AppUtil;
import nguyenhoanganhkhoa.com.thirdlink.ReusedConstraint;

public class OrderDetailScreen extends AppCompatActivity {
    RecyclerView rcvOrderDetail;
    DrinkIncartAdapter adapter;

    ImageView imvArrow;
    LinearLayout layoutViewMorePrice;

    TextView txtPrice, txtDeliveryFee, txtDiscount, txtTotalPayment, txtPaymentMethod, txtStatus, txtOrderID, txtDiscountDelivery;
    TextView txtTextPayment;
    Button btnCancelOrder;

    ConstraintLayout layoutExpanded;

    ReusedConstraint reusedConstraint = new ReusedConstraint();



    private void linkView() {
        rcvOrderDetail = findViewById(R.id.rcvOrderDetail);

        txtPrice = findViewById(R.id.txtPrice);
        txtDeliveryFee = findViewById(R.id.txtDeliveryFee);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtTotalPayment = findViewById(R.id.txtTotalPayment);
        txtPaymentMethod = findViewById(R.id.txtPaymentMethod);
        txtStatus = findViewById(R.id.txtStatus);
        txtOrderID = findViewById(R.id.txtOrderID);

        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        txtDiscountDelivery = findViewById(R.id.txtDiscountDelivery);
        txtTextPayment = findViewById(R.id.txtTextPayment);
        imvArrow = findViewById(R.id.imvArrow);
        layoutViewMorePrice = findViewById(R.id.layoutViewMorePrice);
        layoutExpanded = findViewById(R.id.layoutExpanded);
    }

    private final int GREY = R.color.xamBlcok;
    private final int BLACK = R.color.black;
    private final int YELLOW = R.color.primary_yellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_screen);

        linkView();
        getListDrinkPurchaseDetail();
        setValue();
        initAdapter();



        addEvents();
    }

    PurchaseItem purchaseItem;

    private String getPurchaseID(){
        return getIntent().getStringExtra(AppUtil.MY_BUNDLE);
    }

    private List<DrinkInCart> getListDrinkPurchaseDetail(){
        // L???y d??? li???u l??c ?????u g??n cho purchaseItem n??n m???i c???n ph???i g???i
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(AppUtil.MY_BUNDLE_TRANS);
        if(bundle!=null){
            purchaseItem = (PurchaseItem) bundle.getSerializable(AppUtil.SELECTED_ITEM_TRANS);
        }
        return purchaseItem.getListItems();
    }

    private void initAdapter() {
        adapter = new DrinkIncartAdapter(this);
        adapter.setNumScreen(DrinkIncartAdapter.ORDER_DETAIL_SCREEN);
        adapter.setData(getListDrinkPurchaseDetail());
        rcvOrderDetail.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rcvOrderDetail.setAdapter(adapter);
    }

    private double getPrice(){
        DrinkInCart drink;
        double totalPrice = 0;
        for (int i = 0; i < getListDrinkPurchaseDetail().size(); i++){
            drink = getListDrinkPurchaseDetail().get(i);
            totalPrice = totalPrice + drink.getTotalPrice();
        }
        return totalPrice;
    }
    private void changeColor() {
        String typePurchase = purchaseItem.getTypePurchase();
        String paymentMethod = purchaseItem.getPaymentMethod();
        String totalPayment = txtTotalPayment.getText().toString();
        String text1 = "";
        String text2 = "";


        if(paymentMethod.equals(PurchaseSLSpaceScreen.CASH)){
            text1 = "Please pay ";
            text2 = " VND upon delivery";

            txtPaymentMethod.setText("Payment by cash");
            reusedConstraint.changeColor(txtPaymentMethod,10,txtPaymentMethod.length(),R.color.primary_yellow,this);
        }
        if(paymentMethod.equals(PurchaseSLSpaceScreen.UELCAMP)){
            text1 = "You have paid ";
            text2 = " VND by UEL Camp";

            txtPaymentMethod.setText("Payment by UEL Camp");
            reusedConstraint.changeColor(txtPaymentMethod,10,txtPaymentMethod.length(),R.color.primary_yellow,this);
        }


        txtTextPayment.setText(text1 + txtTotalPayment.getText().toString() + text2);
        reusedConstraint.changeColor(txtTextPayment,text1.length(),text1.length() + totalPayment.length() + 4,R.color.primary_yellow,this);


    }

    private void setValue(){

        double deliveryFee = purchaseItem.getDeliveryFee();
        double discount = purchaseItem.getDiscount();
        double discountDelivery = purchaseItem.getDiscountDeliveryFee();

        if(discountDelivery == 0){
            txtDiscountDelivery.setText("-");
        }
        else{
            txtDiscountDelivery.setText(reusedConstraint.formatCurrency(discountDelivery) + " VND");
        }

        if(discount == 0){
            txtDiscount.setText("-");
        }
        else{
            txtDiscount.setText(reusedConstraint.formatCurrency(discount) + " VND");

        }

        if(deliveryFee == 0){
            txtDeliveryFee.setText("-");
        }
        else{
            txtDeliveryFee.setText(reusedConstraint.formatCurrency(deliveryFee) + " VND");
        }

        txtPrice.setText(reusedConstraint.formatCurrency(getPrice()) + " VND");
        txtTotalPayment.setText(reusedConstraint.formatCurrency(getPrice() + deliveryFee - discount - discountDelivery));

        changeColor();

//        if(!AppUtil.statusOrder.isEmpty()){
//            txtStatus.setText(AppUtil.statusOrder);
//        }

        txtStatus.setText(purchaseItem.getTypePurchase());
        if(txtStatus.getText().toString().equals(PurchaseAdapter.TYPE_PENDING)){
            changeButtonStatus(BLACK,true);
        }
        else if(txtStatus.getText().toString().equals(PurchaseAdapter.TYPE_COMPLETED)){

            changeButtonStatus(YELLOW,true);
            btnCancelOrder.setText(R.string.evaluate);
        }
        else{
            changeButtonStatus(GREY,false);
        }

        txtOrderID.setText(getPurchaseID());
    }

    private void changeButtonStatus(int color, boolean isEnable){
        btnCancelOrder.setBackgroundTintList(getColorStateList(color));
        btnCancelOrder.setEnabled(isEnable);
    }

    @Override
    public void onBackPressed() {
        reusedConstraint.checkNavStatusComeBack(this);
    }

    private void pushData() {
        Intent intent = new Intent(this, EvaluateSLSpaceScreen.class);
        intent.putExtra(AppUtil.MY_BUNDLE_TRANS, (Serializable) getListDrinkPurchaseDetail());
        startActivity(intent);
    }

    boolean isExpanded = false;
    private void addEvents() {
        reusedConstraint.openNav(this);
        reusedConstraint.setActionComeBack(this);
        layoutExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExpanded){
                    layoutViewMorePrice.setVisibility(View.VISIBLE);
                    imvArrow.setImageResource(R.drawable.ic_arrrow_dropdown_up_black);
                    isExpanded = true;
                }
                else{
                    layoutViewMorePrice.setVisibility(View.GONE);
                    imvArrow.setImageResource(R.drawable.ic_arror_down_spinner_black);
                    isExpanded = false;
                }
            }
        });
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtStatus.getText().toString().equals(PurchaseAdapter.TYPE_COMPLETED)){
                    pushData();
                }
                else{
                    CustomDialogTwoButtonNew dialog = new CustomDialogTwoButtonNew(OrderDetailScreen.this);
                    dialog.txtHeaderDialog.setText("Cancel order");
                    dialog.txtContentDialog.setText("You are cancelling your order.\nAre you sure?");
                    dialog.btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.btnOK.setText(R.string.yes);
                    dialog.btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(OrderDetailScreen.this, "Your order has been cancelled!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }



}