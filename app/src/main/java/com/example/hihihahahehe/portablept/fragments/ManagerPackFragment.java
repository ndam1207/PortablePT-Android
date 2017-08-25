package com.example.hihihahahehe.portablept.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hihihahahehe.portablept.R;
import com.example.hihihahahehe.portablept.models.FaceBookModel;
import com.example.hihihahahehe.portablept.models.JSONModel.PackJSONModel;
import com.example.hihihahahehe.portablept.models.PackModel;
import com.example.hihihahahehe.portablept.networks.RetrofitFactory;
import com.example.hihihahahehe.portablept.networks.services.GetZumbaPacks;
import com.example.hihihahahehe.portablept.utils.RealmHandle;
import com.example.hihihahahehe.portablept.utils.ScreenManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerPackFragment extends Fragment {
    private FragmentManager fm;
    private List<PackModel> packModelList = new ArrayList<>();
    private FaceBookModel faceBookModel;
//    private PackAdapter packAdapter;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.fab_add_pack)
    FloatingActionButton fabAddPack;
    @BindView(R.id.rv_packs)
    RecyclerView rvPacks;

    public ManagerPackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_pack, container, false);

        setupUI(view);
//        loadData();
        return view;
    }

    private void setupListPack() {
//        packAdapter = new PackAdapter(packModelList, getContext());
//        rvPacks.setAdapter(packAdapter);
//        rvPacks.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadData() {
        final GetZumbaPacks getZumbaPacks = RetrofitFactory.getInstance().create(GetZumbaPacks.class);
        faceBookModel = RealmHandle.getData();
        getZumbaPacks.getZumbaPacks().enqueue(new Callback<List<PackJSONModel>>() {
            @Override
            public void onResponse(Call<List<PackJSONModel>> call, Response<List<PackJSONModel>> response) {
                if (response.body() != null) {
                    for (PackJSONModel packJSONModel : response.body()) {
                        PackModel packModel = new PackModel();
                        packModel.setPackName(packJSONModel.getPackName());
                        packModel.setCoachName(faceBookModel.getLast_Name() + " " + faceBookModel.getFirst_Name());
                        packModel.setType(packJSONModel.getPurpose());
                        packModel.setPrice(packJSONModel.getPrice());
                        packModel.setDuration(packJSONModel.getDuration());
                        packModel.setImageUrl(packJSONModel.getPackImgUrl());

                        packModelList.add(packModel);
                    }
//                    packAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PackJSONModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        setupListPack();
        setOnClickItem();
    }

    private void setOnClickItem() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        fabAddPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenManager.openFragment(getActivity().getSupportFragmentManager(), new CreatePackFragment(), R.id.layout_container, true);
            }
        });
    }

}
