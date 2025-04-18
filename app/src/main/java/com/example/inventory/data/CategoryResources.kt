package com.example.inventory.data

import com.example.inventory.R
import java.util.ArrayList

class CategoryResources(){

    fun loadCategoryData(): List<CategoryType> {

        val foodBeveragesItems = arrayListOf(
            CategoryLists(R.string.item1, R.drawable.food, "food", "Expense"),
            CategoryLists(R.string.item1, R.drawable.bread, "bread", "Expense"),
            CategoryLists(R.string.item1, R.drawable.ice_cream, "ice_cream", "Expense"),
            CategoryLists(R.string.item1, R.drawable.food1, "dinning", "Expense"),
            CategoryLists(R.string.item1, R.drawable.beer, "beer", "Expense"),
            CategoryLists(R.string.item1, R.drawable.wine, "wine", "Expense"),
            CategoryLists(R.string.item1, R.drawable.wine_bottle, "wine_bottle", "Expense"))

        val utilityItems = arrayListOf(
            CategoryLists(R.string.item2, R.drawable.utilities, "Electricity", "Expense"),
            CategoryLists(R.string.item2, R.drawable.water, "Water", "Expense"),
            CategoryLists(R.string.item2, R.drawable.car_service, "Car Service", "Expense"))

        val entertainmentItems = arrayListOf(
            CategoryLists(R.string.item2, R.drawable.movie, "movie", "Expense"),
            CategoryLists(R.string.item2, R.drawable.sport, "sport", "Expense"),
            CategoryLists(R.string.item2, R.drawable.game_card, "game_cards", "Expense"),
            CategoryLists(R.string.item2, R.drawable.app_games, "app_games", "Expense"))

        val transportItems = arrayListOf(
            CategoryLists(R.string.item2, R.drawable.transport, "Train", "Expense"),
            CategoryLists(R.string.item2, R.drawable.grab_car, "Grab car", "Expense"),
            CategoryLists(R.string.item2, R.drawable.petrol, "Petrol", "Expense"),
            CategoryLists(R.string.item2, R.drawable.car_charge, "Car Charge", "Expense"),
            CategoryLists(R.string.item2, R.drawable.car_wash, "Car wash", "Expense"),
            CategoryLists(R.string.item2, R.drawable.fine, "Car fine", "Expense"),
            CategoryLists(R.string.item2, R.drawable.parking, "Parking", "Expense"),
            CategoryLists(R.string.item2, R.drawable.truck, "Truck", "Expense"),
            CategoryLists(R.string.item2, R.drawable.bus, "Bus", "Expense"),
            CategoryLists(R.string.item2, R.drawable.bicycle, "Bicycle", "Expense"),
            CategoryLists(R.string.item2, R.drawable.motorcycle, "Motorcycle", "Expense"),
            CategoryLists(R.string.item2, R.drawable.taxi, "Taxi", "Expense"),
            CategoryLists(R.string.item2, R.drawable.travel, "Travel", "Expense"),
            CategoryLists(R.string.item2, R.drawable.cruise, "Travel", "Expense"))

        val medicalItems = arrayListOf(
            CategoryLists(R.string.item2, R.drawable.medical, "medical", "Expense"))

        val beautyNutritionItems = arrayListOf(
            CategoryLists(R.string.item2, R.drawable.beauty, "cosmetic", "Expense"),
            CategoryLists(R.string.item2, R.drawable.makeup, "makeup", "Expense"),
            CategoryLists(R.string.item2, R.drawable.skincare, "skin care", "Expense"),
            CategoryLists(R.string.item2, R.drawable.skin_mask, "skin mask", "Expense"),
            CategoryLists(R.string.item2, R.drawable.toothpaste, "toothpaste", "Expense"),
            CategoryLists(R.string.item2, R.drawable.shampoo, "shampoo", "Expense"),
            CategoryLists(R.string.item2, R.drawable.hand_soap, "hand soap", "Expense"),
            CategoryLists(R.string.item2, R.drawable.hair_cut, "hair cut", "Expense"),
            CategoryLists(R.string.item2, R.drawable.finger_beauty, "finger beauty", "Expense"),
            CategoryLists(R.string.item2, R.drawable.beauty_surgery, "beauty surgery", "Expense"),
            CategoryLists(R.string.item2, R.drawable.contact_lenses, "contact lenses", "Expense"))


        return listOf<CategoryType>(
            CategoryType("Food & Beverages", foodBeveragesItems),
            CategoryType("Utilities", utilityItems),
            CategoryType("Entertainment", entertainmentItems),
            CategoryType("Transportation", transportItems),
            CategoryType("Beauty & Personal care", beautyNutritionItems),
            CategoryType("Medical", medicalItems)
        )

    }
}