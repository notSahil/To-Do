package com.example.todoapp.fragments.list
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.fragments.list.adapter.ListAdapter
import com.example.todoapp.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_list.*
import androidx.appcompat.app.ActionBar as ActionBar1

class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    lateinit var  action : ActionBar1

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }
    override fun onStart() {
        super.onStart()
      bottomNavigationView.background = null  // to make nav backggorund invisible
    //    (activity as AppCompatActivity).supportActionBar?.show()
//        if(activity is AppCompatActivity){
//            (activity as AppCompatActivity).setSupportActionBar(mtoolbar)
//
//        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setHasOptionsMenu(true)

      //  val window : ListFragment = this@ListFragment
       // window.setMenuVisibility(false)
       // val toolbar: Toolbar =(R.id.toolbar) as Toolbar
//        if(activity is AppCompatActivity){
//            (activity as AppCompatActivity).setSupportActionBar(toolbar)
    //}
        (activity as AppCompatActivity).supportActionBar?.hide()
     //   (parentFragment as AppCompatActivity).actionBar?.hide()

    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel



        // Setup RecyclerView
        setupRecyclerview()
       // BottomNavigationView bottomAppBar = getActivity().findViewById(R.id.bottomAppBar)



        // Observe LiveData
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        // Set Menu
       //setHasOptionsMenu(true)
//        var toolbar: Toolbar =

        //set action bar in fragmeents.
//        if(activity is AppCompatActivity){
//            (activity as AppCompatActivity).setSupportActionBar()
//        }
//        (activity as AppCompatActivity).supportActionBar?.title = "Changedd"
//        (activity as AppCompatActivity).supportActionBar?.hide()

        // Hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerview() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        // Swipe to Delete
       // swipeToDelete(recyclerView)
    }

//    private fun swipeToDelete(recyclerView: RecyclerView) {
//        val swipeToDeleteCallback = object : SwipeToDelete() {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
//                // Delete Item
//                mToDoViewModel.deleteItem(deletedItem)
//                adapter.notifyItemRemoved(viewHolder.adapterPosition)
//                // Restore Deleted Item
//                restoreDeletedData(viewHolder.itemView, deletedItem)
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData) {
        val snackBar = Snackbar.make(
                view, "Deleted '${deletedItem.title}'",
                Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.list_fragment_menu, menu)
       // inflater.inflate(R.menu.bottom_nav_menu_list, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, { list ->
            list?.let {
                adapter.setData(it)
            }
        })
    }

    // Show AlertDialog to Confirm Removal of All Items from Database Table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                    requireContext(),
                    "Successfully Removed Everything!",
                    Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}