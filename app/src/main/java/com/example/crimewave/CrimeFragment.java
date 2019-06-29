package com.example.crimewave;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.crimewave.Crimes.Crime;
import com.example.crimewave.Crimes.CrimeItemTouchHelper;
import com.example.crimewave.Crimes.CrimeRecyclerAdapter;
import com.example.crimewave.Crimes.FavCrimeDBHelper;
import com.example.crimewave.Crimes.GetCrimes;
import com.example.crimewave.Force.ForcesBasic;
import com.example.crimewave.Force.GetForceAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrimeFragment extends Fragment implements MonthPicker.sendMonthInterface, CrimeItemTouchHelper.CrimeItemTouchHelperListner {

    private static final String TAG = "CrimeFragment";

    //flags
    boolean latlongisopen      = false;
    boolean neighbouringisopen = false;
    boolean isExpanded = false;
    boolean isForceSelected = false;

    Snackbar loadsnack;

    //LatLng views
    CardView       LatLgnCard;
    RelativeLayout LatLngShowlayout;
    EditText       enterLatitude, enterLongitudde;
    Switch       IsExpandedSearch;
    Spinner      LatLngCat;
    TextView     PickmonthBtnLatLng;
    Button       LatLngSearchBtn;
    RecyclerView CrimeLatLngRecView;

    //Neighbouring search views
    CardView       NeighbouringCardView;
    RelativeLayout NeighbouringShowLayout;
    Spinner        PickForceSpinner;
    Spinner        PickNeighbourhoodspinner;
    Spinner        NeighCat;
    TextView       PickMonthBtnNeihg;
    Button         NeighbourSearchBtn;
    RecyclerView   CrimeNeighbourRecView;

    //ArrayLists
    ArrayList < Crime >         LatLngCrimes;
    ArrayList < Crime >         NeighbourhoodCrimes;
    ArrayList < ForcesBasic >   Forces;
    ArrayList < String >        Forces_Names;
    ArrayList < Neighbourhood > Neighbourhoods;
    ArrayList < String >        Neighbourhood_names;

    //Arrays
    String[] CatogoryKeys = new String[15];

    //open fragment;
    OpenFragment openFragment;

    //view
    View mView;

    //ArrayAdapters
    ArrayAdapter ForceListAdapter;
    ArrayAdapter NeighbourhoodListAdapter;

    //recycler view Adapters
    CrimeRecyclerAdapter LatlngCrimeAdapter;
    CrimeRecyclerAdapter NeighCrimeAdapter;

    //user inputs
    String LatlongMonth;
    String NeighbourMonth;

    //To send
    String LatLngcatidSel;
    String NeighcatidSel;
    String SelectedForceid;
    String SelectedNeighbourhoodid;

    //SQLite helper
    FavCrimeDBHelper favCrimeDBHelper;

    public void setOpenFragment(OpenFragment openFragment) {
        this.openFragment = openFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loadsnack = Snackbar.make(getActivity().findViewById(android.R.id.content),"Loading",Snackbar.LENGTH_INDEFINITE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),"Search crimes here",Snackbar.LENGTH_SHORT).show();

        mView = inflater.inflate(R.layout.fragment_crime_at_location, container, false);

        // init views
        //LatLong Views
        LatLgnCard = mView.findViewById(R.id.LatLongCard);
        LatLngShowlayout = mView.findViewById(R.id.LatlngShow);
        enterLatitude = mView.findViewById(R.id.LatitudeEnt);
        enterLongitudde = mView.findViewById(R.id.LongitudeEnt);
        IsExpandedSearch = mView.findViewById(R.id.ExpandSearchswitch);
        LatLngCat = mView.findViewById(R.id.LatLngCat);
        PickmonthBtnLatLng = mView.findViewById(R.id.picker_month_latlong);
        LatLngSearchBtn = mView.findViewById(R.id.getcrimelatlongSearch);
        CrimeLatLngRecView = mView.findViewById(R.id.CrimelLatLongList);

        //Neighbour Views
        NeighbouringCardView = mView.findViewById(R.id.NeighbourhoodCard);
        NeighbouringShowLayout = mView.findViewById(R.id.NeighbouringShow);
        PickForceSpinner = mView.findViewById(R.id.pick_force);
        PickNeighbourhoodspinner = mView.findViewById(R.id.pick_Neighbourhood);
        NeighCat = mView.findViewById(R.id.NeighCat);
        PickMonthBtnNeihg = mView.findViewById(R.id.picker_month_Neighbour);
        NeighbourSearchBtn = mView.findViewById(R.id.getcrimeNeighbourSearch);
        CrimeNeighbourRecView = mView.findViewById(R.id.CrimeNeighbourhoodList);

        //init ArrayLists
        Forces_Names = new ArrayList <>();
        Neighbourhood_names = new ArrayList <>();
        Neighbourhoods = new ArrayList <>();

        //init Array
        CatogoryKeys = getResources().getStringArray(R.array.catogory_url);

        //init outputs
        LatLngcatidSel = CatogoryKeys[0];
        NeighcatidSel = CatogoryKeys[0];
        LatlongMonth = "2019-04";
        NeighbourMonth = "2019-04";

        //init Adapter
        ForceListAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,Forces_Names);
        ForceListAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        PickForceSpinner.setAdapter(ForceListAdapter);
        NeighbourhoodListAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,Neighbourhood_names);
        NeighbourhoodListAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        PickNeighbourhoodspinner.setAdapter(NeighbourhoodListAdapter);

        //get Forces
        getForces();

        //init recycler views
        LatlngCrimeAdapter = new CrimeRecyclerAdapter(getContext(),LatLngCrimes,getActivity(),false);
        CrimeLatLngRecView.setAdapter(LatlngCrimeAdapter);
        CrimeLatLngRecView.setItemAnimator(new DefaultItemAnimator());
        CrimeLatLngRecView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        CrimeLatLngRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        NeighCrimeAdapter = new CrimeRecyclerAdapter(getContext(),NeighbourhoodCrimes,getActivity(),false);
        CrimeNeighbourRecView.setAdapter(NeighCrimeAdapter);
        CrimeNeighbourRecView.setItemAnimator(new DefaultItemAnimator());
        CrimeNeighbourRecView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        CrimeNeighbourRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //attach itemTouchHelper
        ItemTouchHelper.SimpleCallback itemTouchHelperlat = new CrimeItemTouchHelper(0,ItemTouchHelper.RIGHT,this,true);
        ItemTouchHelper.SimpleCallback itemTouchHelperNeigh = new CrimeItemTouchHelper(0,ItemTouchHelper.RIGHT,this,false);
        new ItemTouchHelper(itemTouchHelperNeigh).attachToRecyclerView(CrimeNeighbourRecView);
        new ItemTouchHelper(itemTouchHelperlat).attachToRecyclerView(CrimeLatLngRecView);

        //init db helper
        favCrimeDBHelper = new FavCrimeDBHelper(getActivity());

        return mView;
    }

    void getForces(){
        new GetForceAdapter().getForcesListCall().enqueue(new Callback < ArrayList < ForcesBasic > >() {
            @Override
            public void onResponse(Call < ArrayList < ForcesBasic > > call, Response < ArrayList < ForcesBasic > > response) {
                Forces_Names.clear();
                Forces = response.body();
                for (ForcesBasic f: Forces ) {
                    Forces_Names.add(f.getName());
                }
                ForceListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call < ArrayList < ForcesBasic > > call, Throwable t) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No Network connection !!!",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RELOAD", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getForces();
                    }
                });
                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.show();
                Log.e(TAG, "onFailure: ",t);
            }
        });
    }

    private void getNeighbourhoods(){
        Neighbourhood_names.clear();
        new GetCrimes().getNeighbourhoodsCall(SelectedForceid).enqueue(new Callback < ArrayList < Neighbourhood > >() {
            @Override
            public void onResponse(Call < ArrayList < Neighbourhood > > call, Response < ArrayList < Neighbourhood > > response) {
                Neighbourhoods = response.body();

                for (Neighbourhood N: Neighbourhoods ) {
                    Neighbourhood_names.add(N.getName());
                }
                NeighbourhoodListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call < ArrayList < Neighbourhood > > call, Throwable t) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No Network connection !!!",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNeighbourhoods();
                    }
                }).show();
            }
        });
    }

    private void getCrimeAtLatlng(){
        new GetCrimes().getCrimesAtLocation(LatlongMonth,enterLatitude.getText().toString(),enterLongitudde.getText().toString()).enqueue(new Callback < ArrayList < Crime > >() {
            @Override
            public void onResponse(Call < ArrayList < Crime > > call, Response < ArrayList < Crime > > response) {
                LatLngCrimes = response.body();
                LatlngCrimeAdapter.refreshAdapter(response.body());
                Log.d(TAG, "onResponse: latlngcrimes"+response.body());
                if(response.body()!=null && !response.body().isEmpty()) {
                    CrimeLatLngRecView.setVisibility(View.VISIBLE);
                    loadsnack.dismiss();
                } else {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No crimes around here check elsewhere",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call < ArrayList < Crime > > call, Throwable t) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No Network connection !!!",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCrimeAtLatlng();
                    }
                });
            }
        });
    }

    private void getCrimeAroundLatlng(){
        new GetCrimes().getCrimesAroundLocationCall(LatlongMonth,enterLatitude.getText().toString(),enterLongitudde.getText().toString(),LatLngcatidSel).enqueue(new Callback < ArrayList < Crime > >() {
            @Override
            public void onResponse(Call < ArrayList < Crime > > call, Response < ArrayList < Crime > > response) {
                LatLngCrimes = response.body();
                LatlngCrimeAdapter.refreshAdapter(response.body());
                Log.d(TAG, "onResponse: latlngcrimes"+response.body());
                if(response.body()!=null && response.body().size()>1 && !response.body().isEmpty()) {
                    CrimeLatLngRecView.setVisibility(View.VISIBLE);
                    loadsnack.dismiss();
                } else {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No crimes around here check elsewhere",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call < ArrayList < Crime > > call, Throwable t) {
                Log.e(TAG, "onFailure: LatLngSearch",t.getCause() );
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No Network connection !!!",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCrimeAroundLatlng();
                    }
                }).show();
            }
        });
    }

    private void getCrimeInNeigh(){
        new GetCrimes().getNeighbourhoodBoundariescall(SelectedForceid,SelectedNeighbourhoodid).enqueue(new Callback < ArrayList < GetCrimes.Boundary > >() {
            @Override
            public void onResponse(Call < ArrayList < GetCrimes.Boundary > > call, Response < ArrayList < GetCrimes.Boundary > > response) {
                //Log.i(TAG, "onResponse: get boundary"+response.body());
                new GetCrimes().getCrimesInNeighbourhood(NeighcatidSel,response.body(),NeighbourMonth).enqueue(new Callback < ArrayList < Crime > >() {
                    @Override
                    public void onResponse(Call < ArrayList < Crime > > call, Response < ArrayList < Crime > > response) {
                        NeighbourhoodCrimes = response.body();
                        Log.d(TAG, "onResponse: from crimes at neighbourhood "+response.body());
                        NeighCrimeAdapter.refreshAdapter(NeighbourhoodCrimes);
                        Log.d(TAG, "onResponse: latlngcrimes"+response.body());
                        if (response.body() != null) {
                            CrimeNeighbourRecView.setVisibility(View.VISIBLE);
                            loadsnack.dismiss();
                        } else {
                            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No crimes in the selected Neighbourhood search in a different one",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call < ArrayList < Crime > > call, Throwable t) {
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No Network connection !!!",Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getCrimeInNeigh();
                            }
                        }).show();
                    }
                });
            }

            @Override
            public void onFailure(Call < ArrayList < GetCrimes.Boundary > > call, Throwable t) {
                Log.e(TAG, "onFailure: in getting boundaries",t.getCause() );
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No Network connection !!!",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCrimeInNeigh();
                    }
                }).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //onClicks
        //Cards
        LatLgnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!latlongisopen){
                    if(neighbouringisopen) {
                        NeighbouringShowLayout.setVisibility(View.GONE);
                        neighbouringisopen = false;
                    }
                    enterLatitude.setFocusableInTouchMode(true);
                    enterLatitude.requestFocus();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(enterLatitude, InputMethodManager.SHOW_IMPLICIT);
                    LatLngShowlayout.setVisibility(View.VISIBLE);
                    latlongisopen = true;
                }
            }
        });

        NeighbouringCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!neighbouringisopen){
                    if(latlongisopen) {
                        LatLngShowlayout.setVisibility(View.GONE);
                        latlongisopen = false;
                    }
                    NeighbouringShowLayout.setVisibility(View.VISIBLE);
                    neighbouringisopen = true;
                }
            }
        });

        //MonthPickers
        PickmonthBtnLatLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPicker monthPicker = new MonthPicker();
                monthPicker.setTargetFragment(CrimeFragment.this,1);
                monthPicker.show(getFragmentManager(),"MonthPicker");
            }
        });

        PickMonthBtnNeihg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPicker monthPicker = new MonthPicker();
                monthPicker.setTargetFragment(CrimeFragment.this,2);
                monthPicker.show(getFragmentManager(),"MonthPicker");
            }
        });

        //Switch
        IsExpandedSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isExpanded = true;
                    LatLngCat.setVisibility(View.VISIBLE);
                }
                else {
                    isExpanded = false;
                    LatLngCat.setVisibility(View.GONE);
                }
            }
        });

        //Spinners
        LatLngCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView < ? > parent, View view, int position, long id) {
                LatLngcatidSel = CatogoryKeys[position];
            }

            @Override
            public void onNothingSelected(AdapterView < ? > parent) {
            }
        });


        NeighCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView < ? > parent, View view, int position, long id) {
                NeighcatidSel = CatogoryKeys[position];
            }

            @Override
            public void onNothingSelected(AdapterView < ? > parent) {

            }
        });

        PickForceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView < ? > parent, View view, int position, long id) {
                SelectedForceid  = Forces.get(position).getId();
                isForceSelected = true;
                NeighbourhoodListAdapter.clear();
                Log.d(TAG, "onItemSelected: "+SelectedForceid);
                getNeighbourhoods();
            }

            @Override
            public void onNothingSelected(AdapterView < ? > parent) {

                isForceSelected = false;
            }
        });


        PickNeighbourhoodspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView < ? > parent, View view, int position, long id) {
                SelectedNeighbourhoodid = Neighbourhoods.get(position).getId();
                Log.d(TAG, "onItemSelected: Pick Neighbourhood :" + Neighbourhoods.get(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView < ? > parent) {

            }
        });

        //edit text
        enterLatitude.setText("");
        enterLongitudde.setText("");
        enterLongitudde.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    Log.d(TAG, "onKey: enter pressed");
                    enterLongitudde.setEnabled(false);
                    enterLongitudde.setEnabled(true);

                    return true;
                }
                else return false;
            }
        });

        //Buttons
        LatLngSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeLatLngRecView.setVisibility(View.GONE);
                if(enterLatitude.getText().toString().equals("") || enterLongitudde.getText().toString().equals("")){
                    Log.d(TAG, "onClick: search latlng empty");
                }
                else {
                    loadsnack.show();
                    if(isExpanded){
                        getCrimeAroundLatlng();
                    }
                    else {
                        getCrimeAtLatlng();
                    }
                }
            }
        });

        NeighbourSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadsnack.show();
                CrimeNeighbourRecView.setVisibility(View.GONE);
                NeighbourhoodCrimes = null;
                getCrimeInNeigh();
            }
        });

    }


    @Override
    public void sendMonth(String month,int reqcode) {
        if(reqcode == 1){
            LatlongMonth = month;
        }
        if(reqcode == 2){
            NeighbourMonth = month;
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position, @Nullable Boolean isLatLng) {

        Log.d(TAG, "onSwiped: isLatLng"+isLatLng);

        Crime crimeToSave;
        if(isLatLng) {
            crimeToSave = LatLngCrimes.get(position);
            LatlngCrimeAdapter.notifyDataSetChanged();
        } else {
            crimeToSave = NeighbourhoodCrimes.get(position);
            NeighCrimeAdapter.notifyDataSetChanged();
        }

        saveToDB(crimeToSave);
    }


    void saveToDB(final Crime crimeToSave){
        int result = favCrimeDBHelper.AddCrime(crimeToSave);
        String snackText,snackActionText;
        View.OnClickListener snackClicklistener;

        if(result == FavCrimeDBHelper.ALREADY_EXIST) {
            snackText = "Crime Already in favorites";
            snackActionText = "See Favorites";
            snackClicklistener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFragment.LaunchFragment(OpenFragment.FAVORITE);
                }
            };
        } else  if(result == FavCrimeDBHelper.SUCUSS) {
            snackText = "Crime Added To Favorites";
            snackActionText = "See Favorites";
            snackClicklistener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFragment.LaunchFragment(OpenFragment.FAVORITE);
                }
            };
        } else {
            snackText = "Failed To add crime to Favorites";
            snackActionText = "RETRY";
            snackClicklistener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveToDB(crimeToSave);
                }
            };
        }
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),snackText,Snackbar.LENGTH_LONG);
        snackbar.setAction(snackActionText,snackClicklistener);
        snackbar.show();
    }

}
