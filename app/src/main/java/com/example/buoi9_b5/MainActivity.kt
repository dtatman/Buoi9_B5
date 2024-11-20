package com.example.buoi9_b5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // Danh sách tên để hiển thị trong ListView
    private val nameList = mutableListOf("John", "Jane", "Mark", "Emily", "Chris")

    // Biến để chứa vị trí của mục người dùng chọn
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo ListView và Adapter
        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList)
        listView.adapter = adapter

        // Đăng ký ContextMenu cho ListView
        registerForContextMenu(listView)

        // Xử lý khi người dùng chọn mục trong ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            // Cập nhật vị trí mục đã chọn
            selectedPosition = position
        }

        // Xử lý nút "Thêm tên ngẫu nhiên"
        val btnAddRandom: Button = findViewById(R.id.btnAddRandom)
        btnAddRandom.setOnClickListener {
            addRandomName()
        }
    }

    // Tạo ContextMenu khi người dùng nhấn giữ vào một mục trong ListView
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: android.view.View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        // Kiểm tra nếu ListView
        if (v is ListView) {
            val info = menuInfo as AdapterView.AdapterContextMenuInfo
            selectedPosition = info.position
            menu?.add(0, 1, 0, "Chỉnh sửa")
            menu?.add(0, 2, 1, "Xóa")
            menu?.add(0, 3, 2, "Gửi email")
            menu?.add(0, 4, 3, "Gọi điện")
        }
    }

    // Xử lý các mục được chọn trong ContextMenu
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            1 -> { // Chỉnh sửa tên
                showEditDialog()
                true
            }
            2 -> { // Xóa tên
                deleteName()
                true
            }
            3 -> { // Gửi email
                sendEmail()
                true
            }
            4 -> { // Gọi điện
                makePhoneCall()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Hiển thị AlertDialog để chỉnh sửa tên
    private fun showEditDialog() {
        // Nếu không có tên nào được chọn, không làm gì
        if (selectedPosition == -1) return

        val currentName = nameList[selectedPosition]

        val editText = EditText(this)
        editText.setText(currentName)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Chỉnh sửa tên")
            .setView(editText)
            .setPositiveButton("Lưu") { _, _ ->
                val newName = editText.text.toString()
                nameList[selectedPosition] = newName
                updateListView()
                Toast.makeText(this, "Đã lưu tên mới", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .create()

        dialog.show()
    }

    // Xóa tên khỏi danh sách
    private fun deleteName() {
        // Nếu không có tên nào được chọn, không làm gì
        if (selectedPosition == -1) return

        nameList.removeAt(selectedPosition)
        updateListView()
        Toast.makeText(this, "Đã xóa tên", Toast.LENGTH_SHORT).show()
    }

    // Cập nhật lại ListView sau khi chỉnh sửa hoặc xóa tên
    private fun updateListView() {
        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList)
        listView.adapter = adapter
    }

    // Thêm tên ngẫu nhiên vào danh sách
    private fun addRandomName() {
        val randomNames = listOf("Alex", "Sophia", "Mason", "Olivia", "Liam", "Amelia", "Ethan", "Ava")

        // Sinh một tên ngẫu nhiên từ danh sách
        val randomName = randomNames[Random.nextInt(randomNames.size)]

        // Thêm tên vào danh sách
        nameList.add(randomName)

        // Cập nhật ListView
        updateListView()

        // Thông báo cho người dùng
        Toast.makeText(this, "Đã thêm tên ngẫu nhiên: $randomName", Toast.LENGTH_SHORT).show()
    }

    // Gửi email đến một địa chỉ email cố định (hoặc có thể lấy từ ListView)
    private fun sendEmail() {
        // Nếu không có tên nào được chọn, không làm gì
        if (selectedPosition == -1) return

        val selectedName = nameList[selectedPosition]
        val emailAddress = "${selectedName}@example.com" // Địa chỉ email mặc định hoặc có thể lấy từ tên

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$emailAddress")
            putExtra(Intent.EXTRA_SUBJECT, "Chào $selectedName")
            putExtra(Intent.EXTRA_TEXT, "Xin chào, tôi muốn gửi email này để chào hỏi!")
        }
        startActivity(intent)
    }

    // Gọi điện đến một số điện thoại cố định (hoặc có thể lấy từ ListView)
    private fun makePhoneCall() {
        // Nếu không có tên nào được chọn, không làm gì
        if (selectedPosition == -1) return

        val selectedName = nameList[selectedPosition]
        val phoneNumber = "1234567890" // Số điện thoại mặc định hoặc có thể lấy từ tên

        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

        // Kiểm tra xem có ứng dụng gọi điện không
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Không có ứng dụng gọi điện nào", Toast.LENGTH_SHORT).show()
        }
    }
}
