package com.dejeloper.appventas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private var modelList: ArrayList<UserModel> = ArrayList()
    private var onClickItem: ((UserModel) -> Unit)? = null
    private var onClickDeleteItem: ((UserModel) -> Unit)? = null

    fun addItem(items: ArrayList<UserModel>) {
        this.modelList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (UserModel) -> Unit) {
        this.onClickItem = callback
    }

    fun onClickDeleteItem(callback: (UserModel) -> Unit) {
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_user, parent, false)
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val model = modelList[position]
        holder.bindView(model)
        holder.itemView.setOnClickListener { onClickItem?.invoke(model) }
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(model) }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    class UserViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var status = view.findViewById<TextView>(R.id.tvStatus)

        var btnDelete = view.findViewById<TextView>(R.id.btnDelete)

        fun bindView(model: UserModel) {
            id.text = "Id: " + model.id.toString()
            name.text = "Nombre: " + model.name.toString()
            status.text = "Estado: " + if (model.status == 1) "Activo" else "Inactivo"
        }
    }
}