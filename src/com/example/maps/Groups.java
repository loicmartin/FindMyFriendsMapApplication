package com.example.maps;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@EActivity(R.layout.activity_groups)
public class Groups extends Activity{
	@Extra("currentUser")
	User currentUser;
	@RestService
	GroupManager groupClient;
	List<Group> groupList;
	@ViewById
	ListView groupsListView;
	@Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }
	
	@Background
	void refreshGroupList()
	{
		groupList = groupClient.getGroups(currentUser.getId());
		refreshListView();
	}
	
	@UiThread
	void refreshListView()
	{
		ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(this, R.layout.simple_list_item_1,groupList);
		groupsListView.setAdapter(adapter);
	}
	
	@AfterViews
	void loading()
	{
		Log.v("Groups.loading.currentUser",currentUser.toString());
		refreshGroupList();
	}
}
