package com.example.notemanagerapp.ui.fragment;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notemanagerapp.R;
import com.example.notemanagerapp.adapter.DetailItemNoteAdapter;
import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.databinding.FragmentCategoryBinding;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.DetailItemNote;
import com.example.notemanagerapp.ui.dialog.AddCategoryItemDialog;
import com.example.notemanagerapp.ui.viewmodel.CategoryViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private CategoryViewModel viewModel;
    private DetailItemNoteAdapter adapter;
    private FragmentCategoryBinding binding;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.fragmentCategoryRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DetailItemNoteAdapter(this.getContext());
         binding.fragmentCategoryRv.setAdapter(adapter);

         viewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
         viewModel.getListCategoryItem().observe(getViewLifecycleOwner(), categories -> {
             adapter.setDetailItemNoteList(categories);
         });

         new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                 ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
             @Override
             public boolean onMove(@NonNull RecyclerView recyclerView,
                                   @NonNull RecyclerView.ViewHolder viewHolder,
                                   @NonNull RecyclerView.ViewHolder target) {
                 return false;
             }

             @Override
             public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                 List<DetailItemNote> detailItemNoteList = adapter.getDetailItemNoteList();
                 DetailItemNote category = detailItemNoteList.get(position);

                 if (direction == ItemTouchHelper.RIGHT){
                     AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                     builder.setTitle(getString(R.string.title_dialog));
                     builder.setMessage(getString(R.string.massage_dialog));
                     builder.setCancelable(false);

                    builder.setPositiveButton(getString(R.string.yes_dialog),
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewModel.deleteCategoryItem(category).enqueue(
                                    new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call,
                                                       Response<BaseResponse> response) {
                                    if (response.body() != null){
                                        deleteCategory(response.body());
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                    Toast.makeText(requireContext(), t.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });

                    builder.setNegativeButton(getString(R.string.no_dialog),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    viewModel.refreshLiveData();
                                    dialogInterface.cancel();
                                }
                    });

                    AlertDialog dialog =builder.create();
                    dialog.show();

                 }else {
                     callAddCategoryItemDialog(getString( R.string.edit), category.getName());
                     viewModel.refreshLiveData();
                 }
             }
         }).attachToRecyclerView(binding.fragmentCategoryRv);

         binding.fragmentCategoryBtnAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 callAddCategoryItemDialog(getString(R.string.add), getString(R.string.name_category));
                 viewModel.refreshLiveData();
             }
         });

        return view;
    }

    private void deleteCategory (BaseResponse response){
        if (response.getStatus() == Constants.SUCCESSFUL){
            Toast.makeText(getContext(), getString(R.string.delete_successfully),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.delete_error),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshLiveData();
    }

    private void callAddCategoryItemDialog(String str, String nameCategory){
        DialogFragment dialogFragment = AddCategoryItemDialog.newInstance();

        Bundle bundle =  new Bundle();
        bundle.putString(getString(R.string.check_edit),str);
        bundle.putString(getString(R.string.name_category), nameCategory);

        dialogFragment.setArguments(bundle);

        dialogFragment.show(getActivity().getSupportFragmentManager(), "AddCategoryItemDialog");
    }
}