package com.keeran.makeitrain.weathermodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keeran.makeitrain.MainActivity
import com.keeran.makeitrain.R
import com.keeran.makeitrain.weatherdb.WeatherData
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class WeatherForecastAdapter internal constructor(
    val context: Context,
    val activity: MainActivity
) :
    RecyclerView.Adapter<WeatherForecastAdapter.ItemViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var model = ArrayList<WeatherData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerviewitem_main, parent, false)
        val viewHolder = ItemViewHolder(itemView)

        return viewHolder
    }

    internal fun setBlocks(itemData: ArrayList<WeatherData>) {
        this.model = itemData
        Log.i("AdapterCheck", model.toString())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return model.size
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: WeatherData = model[position]

        val sdfDay = SimpleDateFormat("ha, dd MMM")
        val day: String? = sdfDay.format(item.dt * 1000)

        holder.textView_rvItem_itemWeather.text = item.main
        holder.textView_rvItem_itemDateTime.text = day
        val iconUrl = "http://openweathermap.org/img/w/" + item.icon.toString() + ".png"
        Picasso.with(context).load(iconUrl).into(holder.imageView_rvItem_itemIcon)

    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var textView_rvItem_itemDateTime: TextView =
            itemView.findViewById(R.id.textView_rvItem_itemDateTime)
        internal var textView_rvItem_itemWeather: TextView =
            itemView.findViewById(R.id.textView_rvItem_itemWeather)
        internal var imageView_rvItem_itemIcon: ImageView =
            itemView.findViewById(R.id.imageView_rvItem_itemIcon)

    }


}