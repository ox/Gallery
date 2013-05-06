package com.cs314.photoalbum;

import java.util.ArrayList;

public class Photo {

	public String url;
	public String location;
	public ArrayList<String> peopleTags;
	
	public Photo(String url, String location, ArrayList<String>peopleTags) {
		this.url = url;
		this.location = location;
		this.peopleTags = peopleTags;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
