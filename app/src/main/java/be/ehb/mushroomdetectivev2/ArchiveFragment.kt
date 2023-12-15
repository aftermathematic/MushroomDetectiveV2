package be.ehb.mushroomdetectivev2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class ArchiveFragment : Fragment(R.layout.fragment_archive) {

    private lateinit var viewModel: MushroomViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MushroomAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get a reference to the repository
        val mushroomDao = MushroomDatabase.getDatabase(requireActivity().application).mushroomDao()
        val repository = MushroomRepository(mushroomDao)

        // Use the factory to create a ViewModel
        val factory = MushroomViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MushroomViewModel::class.java)

        // Set up RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.recyclerview_archive)
        adapter = MushroomAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Get the ViewModel
        viewModel = ViewModelProvider(this).get(MushroomViewModel::class.java)

        // Observe the LiveData, updating the list when the data changes
        viewModel.allMushrooms.observe(viewLifecycleOwner, Observer { mushrooms ->
            mushrooms?.let {
                adapter.setMushrooms(it)
            }
        })

    }
}