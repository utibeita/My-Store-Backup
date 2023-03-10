package com.example.mystore.ui.cart

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mystore.R
import com.example.mystore.data.models.Notification
import com.example.mystore.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var cartViewModel: CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initialize ViewModel
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        //calculate subtotal, shipping cost and total
        showOrderInfo()

        //setup spinner showing card providers
        setupSpinner()

        binding.makePayment.setOnClickListener {
            if(inputIsValid()) {
                showConfirmationDialog()
                showNotification(
                    message =  "You just paid for some items worth $${cartViewModel.getPrice()}"
                )
                cartViewModel.clearCart()

            }
        }

        binding.ondeliveryPayment.setOnClickListener {
            if(inputIsInvalid()) {
                showConfirmationDialog()
                showNotification(
                    message =  "You just placed an order for some items worth $${cartViewModel.getPrice()}"
                )
                cartViewModel.clearCart()

            }
        }
    }


    //Show user payment notification
    private fun showNotification(message: String) {
        val id = "payment_successful"

        //create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Declare variables
            val channelName = "Payment Confirmation"
            val description = "User just made a successful payment"
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT


            //created the notification channel
            val notificationChannel = NotificationChannel(id, channelName, importance)
            notificationChannel.description = description

            //Assign the channel to the device notification manager
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        //Create the notification builder
        val builder = NotificationCompat.Builder(this, id)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Payment Made")
        builder.setContentText(message)

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(12333, builder.build())

        saveNotification(message)


    }

    private fun saveNotification(message: String) {
        val notification = Notification(System.currentTimeMillis(), message)

        cartViewModel.saveNotification(notification)
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setView(R.layout.layout_order_successful)
            .setPositiveButton("OK", {dialog, which->
                this@CheckoutActivity.finish()
            })
            .setCancelable(false)
            .show()
    }

    private fun inputIsValid(): Boolean {
        val cardNumber = binding.cardNumber.text.toString()
        val cvv = binding.cvv.text.toString()

        if(cardNumber.length < 12){
            Toast.makeText(this, "Invalid Card Number", Toast.LENGTH_LONG).show()
            return false
        }else if(cardNumber.length > 12){
            Toast.makeText(this, "Invalid Card Number", Toast.LENGTH_LONG).show()
            return false
        }else if(cvv.length != 3){
            Toast.makeText(this, "Invalid CVV", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun inputIsInvalid(): Boolean{
        val cardNumber = binding.cardNumber.text.toString()
        val cvv = binding.cvv.text.toString()

        if(cardNumber.length > 1) {
            Toast.makeText(this, "Add Card info", Toast.LENGTH_LONG).show()
            return false
        }else if(cvv.length > 1){
            Toast.makeText(this, "Add Card info", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


    private fun setupSpinner() {
        val listOfCardProviders = listOf<String>("Visa", "MasterCard", "Verve")
        binding.cardName.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOfCardProviders
        )
    }

    private fun showOrderInfo() {
        val subtotal: Double = cartViewModel.getPrice()
        val shippingCost: Double = 0.1 * subtotal
        val total = subtotal + shippingCost

        binding.subtotal.text = "$$subtotal"
        binding.shippingCost.text = "$$shippingCost"
        binding.total.text = "$$total"
    }
}