package com.example.hihihahahehe.portablept.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hihihahahehe.portablept.R;
import com.example.hihihahahehe.portablept.adapters.PackAdapter;
import com.example.hihihahahehe.portablept.events.PushDataPacks;
import com.example.hihihahahehe.portablept.events.PushDataSports;
import com.example.hihihahahehe.portablept.fragments.typeofpacks.DetailFragmentTest;
import com.example.hihihahahehe.portablept.models.HotCoachesModel;
import com.example.hihihahahehe.portablept.models.HotSportsModel;
import com.example.hihihahahehe.portablept.models.JSONModel.GetPackJSONModel;
import com.example.hihihahahehe.portablept.models.JSONModel.PackJSONModel;
import com.example.hihihahahehe.portablept.models.PackModel;
import com.example.hihihahahehe.portablept.networks.RetrofitFactory;
import com.example.hihihahahehe.portablept.networks.services.GetPackType;
import com.example.hihihahahehe.portablept.utils.ScreenManager;
import com.example.hihihahahehe.portablept.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Array;
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
public class PackFragment extends Fragment {
    public static final String TAG = PackFragment.class.toString();
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    int checkdata=0;

    private List<HotSportsModel> hotSportsModelList;
    @BindView(R.id.rv_pack)
    RecyclerView  rvHotPacks;
    private PackAdapter hotPackAdapter;
    private List<PackModel> hotPackModelList = new ArrayList<>();

    public PackFragment() {
        // Required empty public constructor
    }
    public void  loadPack(HotSportsModel hotSportsModel){
        hotPackModelList.clear();
        GetPackType getPackType = RetrofitFactory.getInstance().create(GetPackType.class);
        getPackType.getPacks(hotSportsModel.getName()).enqueue(new Callback<List<GetPackJSONModel>>() {
            @Override
            public void onResponse(Call<List<GetPackJSONModel>> call, Response<List<GetPackJSONModel>> response) {
                for(GetPackJSONModel packJSONModel : response.body()){
                    PackModel hotPackModel = new PackModel();
                    hotPackModel.setCoachName(packJSONModel.getCoach().getName());
                    hotPackModel.setCost(packJSONModel.getPrice());
                    hotPackModel.setDuration(packJSONModel.getDuration());
                    hotPackModel.setPackName(packJSONModel.getPackName());
                    hotPackModel.setGoal(packJSONModel.getPurpose());
                    hotPackModel.setImg(packJSONModel.getPackImgUrl());
                    hotPackModel.setCoach(packJSONModel.getCoach());
                    hotPackModel.setType(packJSONModel.getType());
                    hotPackModel.setContent(packJSONModel.getContent());
                    hotPackModel.setContent(packJSONModel.getContent());
                    hotPackModel.setId(packJSONModel.getId());
                    if(packJSONModel.getTotalStars() != null && packJSONModel.getVotedStars() != null){
                        hotPackModel.setStars((int)(packJSONModel.getTotalStars().intValue()/packJSONModel.getVotedStars().intValue()));
                    }
                    hotPackModelList.add(hotPackModel);
                }
                hotPackAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<List<GetPackJSONModel>> call, Throwable t) {

            }
        });



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pack, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final LinearLayoutManager linearLayoutManagerPacks = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        hotPackAdapter = new PackAdapter(hotPackModelList, getContext());
        rvHotPacks.setAdapter(hotPackAdapter);
        hotPackAdapter.setOnclikListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackModel  packModel = (PackModel) view.getTag();
                EventBus.getDefault().postSticky(packModel);
                ScreenManager.replaceFragment(getActivity().getSupportFragmentManager(), new DetailFragmentTest(), R.id.layout_container, true);

            }
        });
        rvHotPacks.setLayoutManager(linearLayoutManagerPacks);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (hotSportsModelList!=null){
                    HotSportsModel hotSportsModel = hotSportsModelList.get(tab.getPosition());
                    loadPack(hotSportsModel);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
    @Subscribe(sticky = true)
    public  void onRecivedPack (PushDataSports pushDataSports){

        if (checkdata==0){
            hotSportsModelList = pushDataSports.hotSportsModelList;
            tabLayout.removeAllTabs();
            for(int i = 0; i < hotSportsModelList.size(); i++){
                Utils.addTab(tabLayout,hotSportsModelList.get(i).getName());
            }
            HotSportsModel hotSportsModel  = hotSportsModelList.get(0);
            loadPack(hotSportsModel);
            checkdata=1;
        }

    }





}
