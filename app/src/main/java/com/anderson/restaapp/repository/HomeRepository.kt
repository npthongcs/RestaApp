package com.anderson.restaapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anderson.restaapp.model.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class HomeRepository {

    private val database = Firebase.database.reference
    private val user = Firebase.auth.currentUser
    private var foodLiveData = MutableLiveData<ItemFood>()
    private var statusFoodLiveData = MutableLiveData<String>()
    private var keysFood = ArrayList<String>()

    private var drinkLiveData = MutableLiveData<ItemFood>()
    private var statusDrinkLiveData = MutableLiveData<String>()
    private var keysDrink = ArrayList<String>()

    private var dessertLiveData = MutableLiveData<ItemFood>()
    private var statusDessertLiveData = MutableLiveData<String>()
    private var keysDessert = ArrayList<String>()

    private var quantityAvailableLiveData = MutableLiveData<Quantity>()

    private var invoiceLivedata = MutableLiveData<DetailBooking>()
    private var keysInvoice = ArrayList<String>()

    private var reviewLiveData = MutableLiveData<Review>()
    private var keysReview = ArrayList<String>()

    fun foodLiveDataObserver(): MutableLiveData<ItemFood>{
        return foodLiveData
    }
    fun statusFoodLiveDataObserver(): MutableLiveData<String>{
        return statusFoodLiveData
    }
    fun keysFoodSize(): Int {
        return keysFood.size
    }

    fun drinkLiveDataObserver(): MutableLiveData<ItemFood>{
        return drinkLiveData
    }
    fun statusDrinkLiveDataObserver(): MutableLiveData<String>{
        return statusDrinkLiveData
    }
    fun keysDrinkSize(): Int {
        return keysDrink.size
    }

    fun dessertLiveDataObserver(): MutableLiveData<ItemFood>{
        return dessertLiveData
    }
    fun statusDessertLiveDataObserver(): MutableLiveData<String>{
        return statusDessertLiveData
    }
    fun keysDessertSize(): Int {
        return keysDessert.size
    }

    fun quantityAvailableLiveDataObserver(): MutableLiveData<Quantity>{
        return quantityAvailableLiveData
    }

    fun invoiceLiveDataObserver(): MutableLiveData<DetailBooking>{
        return invoiceLivedata
    }
    fun keysInvoiceSize():Int {return keysInvoice.size}

    fun reviewLiveDataObserver(): MutableLiveData<Review>{
        return reviewLiveData
    }
    fun keysReviewSize(): Int {
        return keysReview.size
    }

    fun processListFood(){
        val query = database.child("Foods")
        query.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val food = snapshot.getValue(ItemFood::class.java)
                val key = snapshot.key
                if (key != null) {
                    keysFood.add(key)
                    foodLiveData.value = food!!
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(ItemFood::class.java)
                val key = snapshot.key
                val index = keysFood.indexOf(key)
                if (index>-1 && item!=null) {
                    val message =
                        "change\t$index\t${item.name}\t${item.price}\t${item.url}\t${item.description}\t${item.discount}"
                    statusFoodLiveData.value = message
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val key = snapshot.key
                val index = keysFood.indexOf(key)
                if (index>-1) {
                    val message = "remove\t$index"
                    statusFoodLiveData.value = message
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("process list food","failed")
            }
        })
    }

    fun processListDrink(){
        val query = database.child("Drinks")
        query.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val drink = snapshot.getValue(ItemFood::class.java)
                val key = snapshot.key
                if (key != null) {
                    keysDrink.add(key)
                    drinkLiveData.value = drink!!
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(ItemFood::class.java)
                val key = snapshot.key
                val index = keysDrink.indexOf(key)
                if (index>-1 && item!=null) {
                    val message =
                        "change\t$index\t${item.name}\t${item.price}\t${item.url}\t${item.description}\t${item.discount}"
                    statusDrinkLiveData.value = message
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val key = snapshot.key
                val index = keysDrink.indexOf(key)
                if (index>-1) {
                    val message = "remove\t$index"
                    statusDrinkLiveData.value = message
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("process list drink","failed")
            }
        })
    }

    fun processListDessert(){
        val query = database.child("Desserts")
        query.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val dessert = snapshot.getValue(ItemFood::class.java)
                val key = snapshot.key
                if (key != null) {
                    keysDessert.add(key)
                    dessertLiveData.value = dessert!!
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(ItemFood::class.java)
                val key = snapshot.key
                val index = keysDessert.indexOf(key)
                if (index>-1 && item!=null) {
                    val message =
                        "change\t$index\t${item.name}\t${item.price}\t${item.url}\t${item.description}\t${item.discount}"
                    statusDessertLiveData.value = message
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val key = snapshot.key
                val index = keysDessert.indexOf(key)
                if (index>-1) {
                    val message = "remove\t$index"
                    statusDessertLiveData.value = message
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("process list dessert","failed")
            }
        })
    }

    fun processGetQuantityAvailable(data: String) {
        val query = database.child("Calendar").child(data)

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val slot = snapshot.getValue(Quantity::class.java)
                if (slot==null) quantityAvailableLiveData.value = Quantity(-1,-1)
                else quantityAvailableLiveData.value = slot!!
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("get quantity","failed")
            }

        })
    }

    fun processListInvoice() {
        user?.let { database.child("Bookings").child(it.uid) }?.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val invoice = snapshot.getValue(ResponseInvoice::class.java)
                Log.d("invoice",invoice.toString())
                val key = snapshot.key
                if (key != null) {
                    keysInvoice.add(key)
                    if (invoice != null) {
                        processGetListBookInInvoice(user.uid,key,invoice)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("process list invoice","failed")
            }

        })
    }
    fun processGetListBookInInvoice(userid: String, key: String, invoice: ResponseInvoice){
        val query = database.child("Bookings").child(userid).child(key).child("listBook")
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lst = ArrayList<FoodSelected>()
                for (i : DataSnapshot in snapshot.children){
                    val data = i.getValue(FoodBooking::class.java)
                    val item = data?.let { FoodSelected(it.name,it.amountFood,it.payment) }
                    if (item != null) {
                        lst.add(item)
                    }
                }
                val data = DetailBooking(
                    invoice.amountPeople,
                    invoice.date,
                    invoice.time,
                    invoice.note,
                    invoice.count,
                    invoice.totalPayment,
                    invoice.discount,
                    lst,
                    invoice.dateTimePayment
                )
                invoiceLivedata.value = data
                Log.d("detail invoice",data.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("process list booking","failed")
            }

        })
    }

    fun processListReview(){
        val query = database.child("Review")
        query.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val review = snapshot.getValue(Review::class.java)
                val key = snapshot.key
                if (key != null) {
                    keysReview.add(key)
                    reviewLiveData.value = review!!
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}