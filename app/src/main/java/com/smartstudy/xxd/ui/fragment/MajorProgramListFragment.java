package com.smartstudy.xxd.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.MajorProgramInfo;
import com.smartstudy.xxd.ui.adapter.hotmajor.MajorProgramAdapter;

import java.util.List;


public class MajorProgramListFragment extends BaseFragment {

    private RecyclerView rvPrograms;
    private MajorProgramAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_major_program_list;
    }

    public static MajorProgramListFragment getInstance(Bundle bundle) {
        MajorProgramListFragment fragment = new MajorProgramListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDetach() {
        if (rvPrograms != null) {
            rvPrograms.removeAllViews();
            rvPrograms = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        super.onDetach();
    }

    @Override
    protected void initView() {
        rvPrograms = rootView.findViewById(R.id.rv_programs);
        final GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        // 设置span
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isSectionHeaderPosition(position)
                    || mAdapter.isSectionFooterPosition(position)) {
                    return manager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        manager.setOrientation(GridLayoutManager.VERTICAL);
        rvPrograms.setHasFixedSize(true);
        rvPrograms.setLayoutManager(manager);
        rvPrograms.setNestedScrollingEnabled(false);
        List<MajorProgramInfo.DirectionsInfo> datas = getArguments().getParcelableArrayList("datas");
        mAdapter = new MajorProgramAdapter(mActivity, datas, mActivity.mInflater);
        rvPrograms.setAdapter(mAdapter);
    }
}
