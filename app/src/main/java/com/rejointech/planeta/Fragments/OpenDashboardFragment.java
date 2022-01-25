package com.rejointech.planeta.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.rejointech.planeta.APICalls.APICall;
import com.rejointech.planeta.Adapters.Adapterimgsliderdashboard;
import com.rejointech.planeta.Container.HomeActivityContainer;
import com.rejointech.planeta.R;
import com.rejointech.planeta.Utils.CommonMethods;
import com.rejointech.planeta.Utils.Constants;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OpenDashboardFragment extends Fragment {

    TextView recycleropendashboard_speciename, recycleropendashboard_familyname, recycleropendashboard_datetime,
            recycleropendashboard_Percentage, recycleropendashboard_familynametextin, recycleropendashboard_genusnameinner,
            recycleritem_dashboard_createdby, recycleropendashboard_picby, recycleropendashboard_commonname1, recycleropendashboard_speciesnameinner,
            recycleropendashboard_commonname2, recycleropendashboard_commonname3, recycleropendashboard_gbifid;
    ImageView recycleropendashboard_backbot, recycleropendashboard_image, recycleropendashboard_wiki_bot;
    AppCompatButton recycleropendashboard_savenotebot;
    AppCompatEditText recycleropendashboard_addnoteedittext;
    Context thiscontext;
    String token, postid, notes, noteid;

    SliderView sliderpager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_open_dashboard, container, false);
        initlayout();
        Init_views(root);
        retrievedatafromprefandset();
        button_clicks();

        return root;
    }

    private void initlayout() {
        ((HomeActivityContainer) getActivity()).setToolbarInvisible();
        ((HomeActivityContainer) getActivity()).setfabinvisible();
        ((HomeActivityContainer) getActivity()).setbotVisible();
    }

    private void button_clicks() {
        recycleropendashboard_backbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new DashboardFragment()).commit();

            }
        });
    }

    private void addnotes(Boolean notnotes) {
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.REGISTERPREFS, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.token, "No data found!!!");
        recycleropendashboard_savenotebot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.DisplayShortTOAST(thiscontext, "Saving your Note");
                String notetext = recycleropendashboard_addnoteedittext.getText().toString();
                if (notnotes) {
                    String url = Constants.createnoteurl;
                    APICall.okhttpmaster().newCall(APICall.post4createnotes(
                            APICall.urlbuilderforhttp(url),
                            token,
                            APICall.buildrequest4createnote(postid, notetext)
                    )).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommonMethods.DisplayLongTOAST(thiscontext, e.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final String myResponse = response.body().string();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responsez = new JSONObject(myResponse);
                                        String status = responsez.optString("status");
                                        if (status.equals("success")) {
                                            CommonMethods.DisplayShortTOAST(thiscontext, "Note Added Successfully\nGo to Notes for checking it out!!");
                                        } else {
                                            CommonMethods.DisplayShortTOAST(thiscontext, "Some Error occurred");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    recycleropendashboard_addnoteedittext.setHint(notes);
                    String url = Constants.updatenoteurl;
                    APICall.okhttpmaster().newCall(APICall.patch4updatenotes(APICall.urlbuilderforhttp(url),
                            APICall.buildrequest4updatingnote(noteid, notetext))).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommonMethods.DisplayLongTOAST(thiscontext, e.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final String myResponse = response.body().string();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject responsez = new JSONObject(myResponse);
                                        String status = responsez.optString("status");
                                        if (status.equals("success")) {
                                            CommonMethods.DisplayShortTOAST(thiscontext, "Note Updated Successfully\nGo to Notes for checking it out!!");
                                        } else {
                                            CommonMethods.DisplayShortTOAST(thiscontext, "Some Error occurred");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });


                }
            }
        });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        thiscontext = context;
    }

    private void retrievedatafromprefandset() {
        SharedPreferences sharedPreferences = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS, Context.MODE_PRIVATE);
        String createdBy = sharedPreferences.getString(Constants.prefdashboardcreatedby, "No data found!!!");
        String timestamp = sharedPreferences.getString(Constants.prefdashboardtimestamp, "No data found!!!");
        String wikkipediaLink = sharedPreferences.getString(Constants.prefdashboardwikilink, "No data found!!!");
        sendwikilink(wikkipediaLink);
        String userimage = sharedPreferences.getString(Constants.prefdashboardusername, "No data found!!!");
        String species_scientificnametrue = sharedPreferences.getString(Constants.prefdashboardspeciessceintific_nametrue, "No data found!!!");
        String species_scientificname = sharedPreferences.getString(Constants.prefdashboardspeciessceintific_name, "No data found!!!");
        String genus_scientifiname = sharedPreferences.getString(Constants.prefdashboardgenus_scientificname, "No data found!!!");
        String family_scientifiname = sharedPreferences.getString(Constants.prefdashboardgenus_familyname, "No data found!!!");
        String percentagetoprint = sharedPreferences.getString(Constants.prefdashboardgenus_score, "No data found!!!");
        String gbif = sharedPreferences.getString(Constants.prefdashboardgenus_gbif, "No data found!!!");
        postid = sharedPreferences.getString(Constants.prefdashboardgenus_postid, "No data found!!!");
        Set<String> commonnameset = sharedPreferences.getStringSet(Constants.prefdashboardgenus_commonnames, null);
        Set<String> resultimageset = sharedPreferences.getStringSet(Constants.prefdashboardgenus_resultimages, null);
        ArrayList<String> commonnames = new ArrayList<String>();
        commonnames.addAll(commonnameset);
        ArrayList<String> resultImages_array = new ArrayList<String>();
        resultImages_array.addAll(resultimageset);

        CommonMethods.LOGthesite(Constants.LOG, resultImages_array.toString());

        String fromnotes = sharedPreferences.getString(Constants.prefdashboard_fromnotes, "0");
        if (sharedPreferences.getString(Constants.prefdashboard_fromcameraidentification, "0").equals("1")) {
            SharedPreferences sharedPreferences5 = thiscontext.getSharedPreferences(Constants.DASHHBOARDPREFS,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor5 = sharedPreferences5.edit();
            editor5.putString(Constants.prefdashboard_fromcameraidentification, "2");
            editor5.apply();
        }
        if (fromnotes.equals("1")) {
            addnotes(false);
            notes = sharedPreferences.getString(Constants.prefdashboardnote, "Not able to derive note");
            noteid = sharedPreferences.getString(Constants.prefdashboardnoteid, "Not able to derive note");
        } else {
            addnotes(true);
        }
        if (commonnames.size() > 2) {
            recycleropendashboard_commonname1.setText(commonnames.get(0));
            recycleropendashboard_commonname2.setText(commonnames.get(1));
            recycleropendashboard_commonname3.setText(commonnames.get(2));
        } else if (commonnames.size() == 2) {
            recycleropendashboard_commonname1.setText(commonnames.get(0));
            recycleropendashboard_commonname2.setText(commonnames.get(1));
        } else if (commonnames.size() == 1) {
            recycleropendashboard_commonname1.setText(commonnames.get(0));
        } else {
            recycleropendashboard_commonname1.setText("No name found");
            recycleropendashboard_commonname2.setText("No name found");
            recycleropendashboard_commonname3.setText("No name found");
        }


        Adapterimgsliderdashboard adapterimgsliderdashboard = new Adapterimgsliderdashboard(resultImages_array);
        sliderpager.setSliderAdapter(adapterimgsliderdashboard);
        sliderpager.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderpager.startAutoCycle();


        recycleropendashboard_speciename.setText(species_scientificname);
        recycleropendashboard_familyname.setText(family_scientifiname);
        recycleropendashboard_datetime.setText(timestamp);
        recycleropendashboard_Percentage.setText(percentagetoprint);
        recycleropendashboard_familynametextin.setText(family_scientifiname);
        recycleropendashboard_genusnameinner.setText(genus_scientifiname);
        recycleropendashboard_speciesnameinner.setText(species_scientificnametrue);
        recycleropendashboard_picby.setText(createdBy);
        recycleropendashboard_gbifid.setText(gbif);
        Picasso.get()
                .load(userimage)
                .error(R.drawable.icontree)
                .into(recycleropendashboard_image);

        recycleropendashboard_wiki_bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.maincontainerview, new WebviewFragment()).addToBackStack(null).commit();
            }
        });

    }

    public boolean indexExists(final ArrayList<String> list, final int index) {
        return index >= 0 && index < list.size();
    }

    private void sendwikilink(String wikkipediaLink) {
        SharedPreferences sharedPreferences1 = thiscontext.getSharedPreferences(Constants.WIKILINK,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString(Constants.prefwikireallinktoopen, wikkipediaLink);
        editor1.commit();
    }

    private void Init_views(View root) {
        recycleropendashboard_speciename = root.findViewById(R.id.recycleropendashboard_speciename);
        recycleropendashboard_familyname = root.findViewById(R.id.recycleropendashboard_familyname);
        recycleropendashboard_datetime = root.findViewById(R.id.recycleropendashboard_datetime);
        recycleropendashboard_Percentage = root.findViewById(R.id.recycleropendashboard_Percentage);
        recycleropendashboard_familynametextin = root.findViewById(R.id.recycleropendashboard_familynametextin);
        recycleropendashboard_genusnameinner = root.findViewById(R.id.recycleropendashboard_genusnameinner);
        recycleritem_dashboard_createdby = root.findViewById(R.id.recycleritem_dashboard_createdby);
        recycleropendashboard_picby = root.findViewById(R.id.recycleropendashboard_picby);
        recycleropendashboard_gbifid = root.findViewById(R.id.recycleropendashboard_gbifid);
        recycleropendashboard_commonname1 = root.findViewById(R.id.recycleropendashboard_commonname1);
        recycleropendashboard_commonname2 = root.findViewById(R.id.recycleropendashboard_commonname2);
        recycleropendashboard_commonname3 = root.findViewById(R.id.recycleropendashboard_commonname3);
        recycleropendashboard_backbot = root.findViewById(R.id.recycleropendashboard_backbot);
        recycleropendashboard_image = root.findViewById(R.id.recyclerallnotes_image);
        recycleropendashboard_wiki_bot = root.findViewById(R.id.recycleropendashboard_wiki_bot);
        recycleropendashboard_savenotebot = root.findViewById(R.id.recycleropendashboard_savenotebot);
        recycleropendashboard_speciesnameinner = root.findViewById(R.id.recycleropendashboard_speciesnameinner);
        recycleropendashboard_addnoteedittext = root.findViewById(R.id.recycleropendashboard_addnoteedittext);
        sliderpager = root.findViewById(R.id.sliderpageree);
/*        recycler_opendashboard_image1 = root.findViewById(R.id.recycler_opendashboard_image1);
        recycler_opendashboard_image2 = root.findViewById(R.id.recycler_opendashboard_image2);
        recycler_opendashboard_image3 = root.findViewById(R.id.recycler_opendashboard_image3);*/
    }
}