package org.szederz.prodsnap.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.szederz.prodsnap.R;
import org.szederz.prodsnap.entities.BasketItem;
import org.szederz.prodsnap.mobile.ApplicationSingleton;
import org.szederz.prodsnap.services.BasketService;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

public class BasketItemAdapter extends RecyclerView.Adapter<BasketItemAdapter.BasketItemViewHolder> {
    private BasketService service;
    private TextView basketItemCount;

    public BasketItemAdapter() {
        this.service = ApplicationSingleton.getService(BasketService.class);
    }

    @NonNull
    @Override
    public BasketItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        basketItemCount = parent.getRootView().findViewById(R.id.basket_count);

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_basket_item, parent, false);
        return new BasketItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketItemViewHolder holder, int position) {
        BasketItem item = service.getBasketItemAt(position);
        holder.name.setText(item.getDetails().getName());
        holder.count.setText(String.valueOf(item.getAmount()));
        holder.removeButton.setOnClickListener(new RemoveBasketItemListener(position));
    }

    @Override
    public int getItemCount() {
        return service.getBasketSize();
    }

    public class BasketItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView count;
        private final AppCompatImageButton removeButton;

        public BasketItemViewHolder(@NonNull final View view) {
            super(view);
            this.name = view.findViewById(R.id.basket_item_name);
            this.count = view.findViewById(R.id.basket_item_count);
            this.removeButton = view.findViewById(R.id.basket_item_delete_button);
        }
    }

    public class RemoveBasketItemListener implements View.OnClickListener {
        private final int position;

        public RemoveBasketItemListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            service.dropItem(position);
            notifyDataSetChanged();
            basketItemCount.setText(String.valueOf(service.getBasketItemCount()));
        }
    }
}
