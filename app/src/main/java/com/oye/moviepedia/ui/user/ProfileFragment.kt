package com.oye.moviepedia.ui.user

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.oye.moviepedia.R
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.databinding.FragmentProfileBinding
import com.oye.moviepedia.domain.uses_cases.GetListsDataError
import com.oye.moviepedia.domain.uses_cases.GetListsError
import com.oye.moviepedia.domain.uses_cases.GetListsSuccess
import com.oye.moviepedia.ui.BaseFragment
import com.oye.moviepedia.ui.home.ListMovieItem
import com.oye.moviepedia.ui.home.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), PlaylistListAdapter.PlaylistListener {

    private val viewModel: UserViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val movieList = ArrayList<ListMovieItem>(4).apply {
        repeat(4) {
            add(ListMovieItem("", mutableListOf()))
        }
    }
    private val playlistList = ArrayList<ListPlaylistItem>(4).apply {
        repeat(4) {
            add(ListPlaylistItem(mutableListOf()))
        }
    }
    private var accountId: String? = null
    private var accessToken: String? = null

    companion object {
        private const val ARG_ACCOUNT_ID = "account_id"
        private const val ARG_ACCESS_TOKEN = "access_token"

        fun newInstance(authData: AuthDto): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(ARG_ACCOUNT_ID, authData.account_id)
            args.putString(ARG_ACCESS_TOKEN, authData.access_token)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            accountId = it.getString(ARG_ACCOUNT_ID)
            accessToken = it.getString(ARG_ACCESS_TOKEN)
        }
        viewModel.init(accessToken, accountId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        setupUI()

        val title = binding.appTitle
        val paint = title.paint
        val width = paint.measureText(title.text.toString())
        title.paint.shader = LinearGradient(
            0f, 0f, width, 0f, Color.parseColor("#9CCCA5"), Color.parseColor("#51B1DF"),
            Shader.TileMode.CLAMP
        )

        val recyclerViewPlaylist = binding.recyclerPlaylist
        val playlistLinearLayoutManager = LinearLayoutManager(container?.context)
        recyclerViewPlaylist.layoutManager = playlistLinearLayoutManager

        movieList.ensureCapacity(4)
        playlistList.ensureCapacity(4)
        initPlaylist()

        return view
    }

    private fun initPlaylist(){
        viewModel.getListsState.observe(viewLifecycleOwner) {
            when (it) {
                is GetListsSuccess -> {
                    if (!viewModel.isPlaylistExists("A voir")) {
                        accessToken?.let { it1 -> viewModel.createPlaylist(it1, "A voir") }
                    } else if(!viewModel.isPlaylistExists("Favoris")){
                        accessToken?.let { it1 -> viewModel.createPlaylist(it1, "Favoris") }
                    }
                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid)
                    val playlists = it.lists.map { e -> PlaylistItem(e.id, drawable!!, e.name, e.number_of_items.toString() + " film(s)") }
                        .toMutableList()
                    playlistList[0] = ListPlaylistItem(playlists)
                    binding.recyclerPlaylist.adapter = ListPlaylistListAdapter(playlistList, activity, this)
                }

                is GetListsDataError -> {
                    Log.e("DATA ERROR", it.ex.message)
                }

                is GetListsError -> {
                    Log.e("ERROR", it.ex.message!!)
                }

                else -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToLoginFragment() {
        val parentFragmentManager = parentFragmentManager
        val loginFragment = LoginFragment()

        parentFragmentManager.beginTransaction()
            .remove(this)
            .commitAllowingStateLoss()

        parentFragmentManager.beginTransaction()
            .replace(R.id.container, loginFragment)
            .commit()
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Déconnexion")
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
        alertDialogBuilder.setPositiveButton("Déconnexion") { dialog, which ->
            SessionManager.logout()
            navigateToLoginFragment()
        }
        alertDialogBuilder.setNegativeButton("Annuler", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showCreatePlaylistDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Nouvelle playlist")

        val inputEditText = EditText(requireContext())
        inputEditText.hint = "Nom de la playlist"
        alertDialogBuilder.setView(inputEditText)

        alertDialogBuilder.setPositiveButton("Créer") { dialog, which ->
            val playlistName = inputEditText.text.toString()
            if (playlistName.isNotEmpty()) {
                if (playlistName.equals("A voir", ignoreCase = true) || playlistName.equals("Favoris", ignoreCase = true)) {
                    Toast.makeText(requireContext(), "Veuillez saisir un autre nom de playlist", Toast.LENGTH_SHORT).show()
                } else {
                    accessToken?.let { it1 -> viewModel.createPlaylist(it1, playlistName) }
                    Toast.makeText(requireContext(), "Playlist créée", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Veuillez saisir un nom de playlist", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialogBuilder.setNegativeButton("Annuler", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    override fun onPlaylistCLick(playlistId: Int) {
        Log.d("log", "playlist id : $playlistId")
        (parentFragment as? UserFragment)?.let {
            it.navigateToDetailPlaylist(playlistId, accessToken!!)
        }
    }

    private fun setupUI() {
        setupSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        binding.toolbar.setNavigationOnClickListener {
            onSupportNavigateUp()
        }
        setupMenu()
    }

    private fun setupMenu() {
        val menuResId : Int = R.menu.profile_menu
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            @SuppressLint("RestrictedApi")
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
                if (menu is MenuBuilder) {
                    menu.setOptionalIconsVisible(true)
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(menuResId, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_logout -> {
                        showLogoutConfirmationDialog()
                    }
                    R.id.action_createPlaylist -> {
                        showCreatePlaylistDialog()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}