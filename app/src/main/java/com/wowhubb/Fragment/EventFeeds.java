package com.wowhubb.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wowhubb.Activity.LandingPageActivity;
import com.wowhubb.Activity.ShareThoughtsActivity;
import com.wowhubb.Activity.ShareThoughtsUrlActivity;
import com.wowhubb.Activity.TodayEventActivity;
import com.wowhubb.Adapter.PaginationAdapter;
import com.wowhubb.FeedsData.Doc;
import com.wowhubb.FeedsData.Feeds;
import com.wowhubb.FeedsData.Message;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;
import com.wowhubb.Utils.ApiClient;
import com.wowhubb.Utils.ApiInterface;
import com.wowhubb.Utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EventFeeds extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = "MainActivity";
    private static final int PAGE_START = 1;
    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    public static PaginationAdapter adapter;
    public static RecyclerView rv;
    public static List<Doc> results;
    LinearLayoutManager linearLayoutManager;
    TextView todayevent;
    ProgressBar progressBar;
    String token, personalimage, selectedCoverFilePath;
    EditText sharethougts_lv;
    ImageView profile_iv;
    LinearLayout piclv;
    FloatingActionButton fab;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int total, limit;
    private int currentPage = PAGE_START;
    private ApiInterface movieService;
    static SearchView searchView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventfeed, container, false);
       // setHasOptionsMenu(true);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getString("token", "");
        personalimage = sharedPreferences.getString("profilepath", "");

        FontsOverride.overrideFonts(getActivity(), view);
        rv = (RecyclerView) view.findViewById(R.id.listview);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        sharethougts_lv = view.findViewById(R.id.sharethougts_lv);
        profile_iv = view.findViewById(R.id.imageview_profile);
        todayevent = view.findViewById(R.id.todayevent);
        piclv = view.findViewById(R.id.piclv);
        fab = view.findViewById(R.id.fab);
        results = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LandingPageActivity.class);
                intent.putExtra("fabstatus", "true");
                startActivity(intent);
            }
        });

        if (personalimage != null) {
            Glide.with(this).load("http://104.197.80.225:3010/wow/media/personal/" + personalimage).into(profile_iv);
        } else {
            profile_iv.setImageResource(R.drawable.profile_img);
        }


        sharethougts_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShareThoughtsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        piclv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShareThoughtsUrlActivity.class);
                startActivity(intent);

            }
        });

        todayevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodayEventActivity.class);
                startActivity(intent);
            }
        });
        adapter = new PaginationAdapter(getActivity(), results);


        adapter.setLoadMoreListener(new PaginationAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("tag", "111111----------->>>>");
                rv.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        isLoading = true;
                        Log.e("tag", "111111----------->>>>");
                        currentPage += 1;
                        Log.e("tag", "111111----------->>>>" + currentPage);
                        loadMore(currentPage);

                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);


        movieService = ApiClient.getClient().create(ApiInterface.class);

        loadFirstPage(1);


        return view;

    }


    private void loadFirstPage(int index) {
        currentPage = index;

        callTopRatedMoviesApi().enqueue(new Callback<Feeds>() {
            @Override
            public void onResponse(Call<Feeds> call, Response<Feeds> response) {

                if (response.isSuccessful()) {

                    results = fetchResults(response);
                    Log.e("tag", "elssssssssssssseeee-ll---------->" + results);
                    adapter.addAll(results);
                    adapter.notifyDataChanged();
                    progressBar.setVisibility(View.GONE);

                    Log.e("tag", "elssssssssssssseeee-ll---------->");
                    /*if (currentPage < TOTAL_PAGES)
                    {
                        if (currentPage == TOTAL_PAGES) {
                            isLastPage = true;
                        } else {
                            Log.e("tag", "TOTAL_PAGES-ll---------->" + TOTAL_PAGES);
                            adapter.addLoadingFooter();
                            currentPage += 1;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadNextPage();
                            }
                            }, 1000);
                        }

                    } else {
                        isLastPage = true;
                    }*/
                } else {

                    Log.e("tag", "elssssssssssssseeee-ll---------->");

                }
            }

            @Override
            public void onFailure(Call<Feeds> call, Throwable t) {
                t.printStackTrace();
                Log.e("tag", "failureeee-ll---------->" + t.getMessage());
                adapter.removeLoadingFooter();
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Connect with friends to see Event feeds", Toast.LENGTH_LONG).show();

            }
        });


    }


    private List<Doc> fetchResults(Response<Feeds> response) {
        Feeds feeds = response.body();
        Log.e("tag", "cppppppp------------" + feeds.getSuccess());
        Message msg = feeds.getMessage();
        TOTAL_PAGES = msg.getPages();
        currentPage = Integer.parseInt(msg.getPage());
        total = msg.getTotal();
        limit = msg.getLimit();
        Log.e("tag", "totallllimit------------->>>" + msg.getTotal() + limit);
        return msg.getDocs();

    }

    private void loadMore(int index) {
        Log.e("tag", "222222----------->>>>" + index);
        currentPage = index;
        callTopRatedMoviesApi().enqueue(new Callback<Feeds>() {
            @Override
            public void onResponse(Call<Feeds> call, Response<Feeds> response) {
                if (response.isSuccessful()) {
                    Log.e("tag", "valll----->" + results);
                    results = fetchResults(response);
                    //remove loading view
                    Log.e("tag", "valll222----->" + results);
                    if (results.size() > 0) {
                        //add loaded data
                        adapter.addAll(results);
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                    } else {//result size 0 means there is no more data available at server
                        adapter.setMoreDataAvailable(false);
                        progressBar.setVisibility(View.GONE);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(getActivity(), "No More Data Available", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                } else {
                    Log.e(TAG, " Load More Response Error " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Feeds> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }

    private void loadNextPage() {
        Log.e("tag", "---cpnexttt------ " + currentPage + TOTAL_PAGES);
        callTopRatedMoviesApi().enqueue(new Callback<Feeds>() {
            @Override
            public void onResponse(Call<Feeds> call, Response<Feeds> response) {
                results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) {
                    Log.e("tag", "---cpnextttiffffffffff-----");
                    adapter.addLoadingFooter();
                    currentPage += 1;
                  /*  new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        //    loadNextPage();
                        }
                    }, 1000);*/
                } else {
                    isLastPage = true;
                    Log.e("tag", "---cpnextttJJJJJJJJJJJJJJJJJJ------ ");

                }
            }

            @Override
            public void onFailure(Call<Feeds> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });

    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<Feeds> callTopRatedMoviesApi() {
        return movieService.checkLogin("application/x-www-form-urlencoded", token, currentPage);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("tag", "<<<<---------------Pause---------->>>>");
        currentPage = 1;
        Log.e("tag",">>>pause-----------");
        //setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("tag", "<<<<---------------Ressume---------->>>>");

        //movieService = ApiClient.getClient().create(ApiInterface.class);
        //  adapter.clear();
        //loadFirstPage();

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_COVERIMAGES) {
                Uri selectedMediaUri = data.getData();
                selectedCoverFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);

                if (selectedCoverFilePath != null && !selectedCoverFilePath.equals("null")) {
                    Intent intent = new Intent(getActivity(), ShareThoughtsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putString("selectedCoverFilePath", selectedCoverFilePath);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }


            }

        }

    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.menu_home, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
     searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed

                        Log.e("tag", "ITEMMMMMM" + results);
                        adapter.setFilter(results);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

        super.onCreateOptionsMenu(menu, inflater);


    }
*/

    @Override
    public boolean onQueryTextChange(String newText) {

        //EventFeeds.adapter.filter(searchQuery.toString().trim());
        // EventFeeds.rv.invalidate();
        Log.e("tag", "ITEMMMMMMresultsssssssss---------" + results);

        final List<Doc> filteredModelList = filter(results, newText.toString().trim());
        Log.e("tag", "ITEMMMMMM111111111111" + filteredModelList);
        adapter.setFilter(filteredModelList);
        //rv.invalidate();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Doc> filter(List<Doc> models, String query) {
        query = query.toLowerCase().trim();

        Log.e("tag", "ITEMMMMMM1222222222" + models);
        final List<Doc> filteredModelList = new ArrayList<>();
        for (Doc model : models) {
            final String text = model.getEventname();

            Log.e("tag", "ITEMMMMMMtextttttttttttttt0000000" + text);

            if (text != null) {
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
        }

        Log.e("tag", "ITEMMMMMMtextttttttttttttt" + filteredModelList);

        return filteredModelList;
    }


}
