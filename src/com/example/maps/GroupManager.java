package com.example.maps;

import java.util.List;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

@Rest(rootUrl="http://192.168.1.90:6525/service/rest", converters={MappingJacksonHttpMessageConverter.class})
public interface GroupManager {
	@Get("/GetGroups?id={id}")
	List<Group> getGroups(String id);
}
