/**
 * Frances Chang: I wrote the fragment for the main page of my fridge.
 */

package edu.uw.peihsi5.lemmeshoyu

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.uw.peihsi5.lemmeshoyu.database.my_fridge_database.Ingredient
import edu.uw.peihsi5.lemmeshoyu.dialogs.FridgeAddItemFragment
import edu.uw.peihsi5.lemmeshoyu.viewmodels.MyFridgeViewModel
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.*
import java.util.*

/**
 * This is a class representing the main page of the my fridge page.
 */
class FridgeHomeFragment : Fragment() {
    private val TAG = "FridgeHomeFragment"
    private lateinit var adapter: FridgeListAdapter
    private lateinit var viewModel: MyFridgeViewModel

    /**
     * Handles functionalities when the fragment is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Handles functionalities when view is created, including setting up the recycler view the displays
     * the items in the fridge.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_fridge_home, container, false)

        val orientation = resources.configuration.orientation

        adapter = FridgeListAdapter(requireContext())
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(MyFridgeViewModel::class.java)

        val resultsObserver = Observer<List<Ingredient>> {
            adapter.submitList(it)
            Log.v(TAG, it.toString())
        }
        viewModel.allIngredients?.observe(viewLifecycleOwner, resultsObserver)

        val recycler = rootView.findViewById<RecyclerView>(R.id.fridge_list)
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            recycler.layoutManager = GridLayoutManager(activity, 2)
        } else {
            // In portrait
            recycler.layoutManager = LinearLayoutManager(activity)
        }
        recycler.adapter = adapter

        rootView.findViewById<FloatingActionButton>(R.id.floating_add_ingredient_button).setOnClickListener {
            // pop up dialog for user to add item
            val dialog = FridgeAddItemFragment()
            dialog.show(requireActivity().supportFragmentManager, "fridgeAddItem")
        }

        return rootView
    }

    /**
     * This class represents the adapter for the recycler view that handles related functionalities.
     */
    inner class FridgeListAdapter(val context: Context) : ListAdapter<Ingredient, FridgeListAdapter.ViewHolder>(FridgeDiffCallback()) {
        /**
         * A ViewHolder that holds date for the specified view.
         */
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val itemDeleteBtn: ImageButton = view.findViewById<ImageButton>(R.id.delete_fridge_item_btn)
            val itemName: TextView = view.findViewById<TextView>(R.id.fridge_list_item_name)
            val itemExpDate: TextView = view.findViewById<TextView>(R.id.fridge_list_item_exp_date)
            val ingredientImage: ImageView = view.findViewById(R.id.ingredient_image)
        }

        /**
         * Handles functionalities when the view holder is created including inflating the layout.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(
                R.layout.fridge_list_item,
                parent,
                false
            )
            return ViewHolder(inflatedView)
        }

        /**
         * Handles functionalities when the view holder is bind including setting up the elements in
         * the inflated layout.
         */
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            holder.itemName.text = item.itemName
            holder.itemExpDate.text = "${item.expireMonth}/${item.expireDay}/${item.expireYear}"
            holder.itemDeleteBtn.setOnClickListener { viewModel.delete(item) }

            // bind ingredient image
            val bitmap = BitmapFactory.decodeByteArray(item.ingredientImage, 0, item.ingredientImage!!.size)
            holder.ingredientImage.setImageBitmap(bitmap)
        }
    }

    /**
     * This class is a callback function for the fridge list adapter that allows us to compare items
     * in the recycler view.
     */
    inner class FridgeDiffCallback: DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem == newItem
        }
    }
}