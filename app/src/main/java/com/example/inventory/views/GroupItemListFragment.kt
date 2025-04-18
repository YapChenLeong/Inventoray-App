package com.example.inventory.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.InventoryApplication
import com.example.inventory.R
import com.example.inventory.adapters.CountryAdapter
import com.example.inventory.adapters.ItemListData
import com.example.inventory.adapters.SampleAdapter
import com.example.inventory.databinding.FragmentGroupItemListBinding
import com.example.inventory.databinding.FragmentStatisticBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class GroupItemListFragment : Fragment() {
    private val navigationArgs: GroupItemListFragmentArgs by navArgs()

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentGroupItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGroupItemListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            show()
            setDisplayShowCustomEnabled(false)
            setDisplayShowTitleEnabled(true)
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE

        val countryName = arguments?.getString("countryName")
        if (!countryName.isNullOrEmpty()) {
            val formattedTitle = getString(R.string.item_detail_fragment_title2, countryName)
            requireActivity().title = formattedTitle
        }

        val groupData = navigationArgs.itemListData
//        val itemListData = arguments?.getParcelable<ItemListData>("item_list_data")
        val itemList = groupData?.items

        val adapter = SampleAdapter {
            val action = GroupItemListFragmentDirections.actionGroupItemListFragmentToItemDetailFragment(it.id)//nav graph
//            val action = ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id) //not nab graph
            this.findNavController().navigate(action)
        }

        binding.groupDataRecycleView.layoutManager = LinearLayoutManager(this.context)
        binding.groupDataRecycleView.adapter = adapter


            adapter.list = itemList!!
            adapter.notifyDataSetChanged()

    }

}