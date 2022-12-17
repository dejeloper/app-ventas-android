package com.dejeloper.appventas

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dejeloper.appventas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var txtName: EditText
    private lateinit var autvStatus: AutoCompleteTextView
    private lateinit var btnAdd: Button

    //    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button

    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: UsersAdapter? = null
    private var model: UserModel? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)
        btnAdd.setOnClickListener { addUser() }
        btnUpdate.setOnClickListener { updateUser() }

        getUsers()
        adapter?.setOnClickItem {
//            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            txtName.setText(it.name)
            autvStatus.setText(if (it.status == 1) "Activo" else "Inactivo", false)
            model = it
        }

        adapter?.onClickDeleteItem {
            deleteUSer(it.id)
        }
    }

    private fun addUser() {
        val name = txtName.text.toString()
        val status = autvStatus.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "Nombre de usuario requerido", Toast.LENGTH_SHORT).show()
        } else if (status == "") {
            Toast.makeText(this, "Estado de usuario requerido", Toast.LENGTH_SHORT).show()
        } else {
            val idStatus = if (status == "Activo") 1 else 0
            val model = UserModel(name = name, status = idStatus)
            val status = sqLiteHelper.insertUser(model)

            if (status > -1) {
                Toast.makeText(this, "Usuario Agregado...", Toast.LENGTH_SHORT).show()
                clearEditText()
                getUsers()
            } else {
                Toast.makeText(this, "El Usuario no pudo ser guardado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUsers() {
        val modalList = sqLiteHelper.getAllUsers()
        adapter?.addItem(modalList)
    }

    private fun updateUser() {
        val name = txtName.text.toString()
        val status = if (autvStatus.text.toString() == "Activo") 1 else 0

        if (name == model?.name && status == model?.status) {
            Toast.makeText(this, "No hay cambios", Toast.LENGTH_SHORT).show()
            return
        }

        if (model == null) {
            Toast.makeText(this, "No hay datos", Toast.LENGTH_SHORT).show()
            return
        }

        val model = UserModel(id = model!!.id, name = name, status = status)
        val success = sqLiteHelper.updateUser(model)

        if (success < 0) {
            Toast.makeText(this, "El Usuario no pudo ser Actualizado", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Usuario Actualizado...", Toast.LENGTH_SHORT).show()
        clearEditText()
        getUsers()
    }

    private fun deleteUSer(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Desea eliminar el usuario?")
        builder.setCancelable(true)
        builder.setPositiveButton("Sí") { dialog, _ ->
            sqLiteHelper.deleteUser(id)
            Toast.makeText(this, "Usuario Eliminado", Toast.LENGTH_SHORT).show()
            clearEditText()
            getUsers()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        txtName.setText("")
        autvStatus.setText("", false)
        txtName.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsersAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        txtName = findViewById(R.id.edName)
        autvStatus = findViewById(R.id.autvStatus)
        btnAdd = findViewById(R.id.btnAdd)
        //btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)

        loadStatus()
        recyclerView = findViewById(R.id.recyclerviewUsers)
    }

    private fun loadStatus() {
        val usersStatus = resources.getStringArray(R.array.StatusUsers)
        val adapter = ArrayAdapter(this, R.layout.list_users_status, usersStatus)

        with(binding.autvStatus) {
            setAdapter(adapter)
            onItemClickListener = this@MainActivity
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
//        Toast.makeText(this@MainActivity, item, Toast.LENGTH_SHORT).show()
    }

}
