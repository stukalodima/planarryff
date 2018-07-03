package com.planarry.erp.web.utils;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SuggestionField;
import com.planarry.erp.service.GeocodingService;
import com.planarry.erp.web.map.MapPicker;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.planarry.erp.web.utils.MapAssistant.ZoomLevel.*;

@Scope("prototype")
@org.springframework.stereotype.Component(AddressPickerUtils.NAME)
public class AddressPickerUtils {

    public static final String NAME = "erp_AddressPickerUtils";

    private final Logger log = LoggerFactory.getLogger(AddressPickerUtils.class);

    private GeocodingService geocodingService = AppBeans.get(GeocodingService.class);

    @Inject
    private Messages messages;

    private Map<String, AddressClassForGoogle> addressMap = new HashMap<>();


    public List<String> getAddressFromGoogle(String value, Frame frame) {
        List<String> addresses = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            String result = geocodingService.findAddressByShirtAddress(value.replaceAll("\\s", "+"));
            parseAddressJson(result, addresses, BUILDING_LEVEL, frame);
        }
        return addresses;
    }

    private void parseAddressJson(String json, List<String> addressList, MapAssistant.ZoomLevel zoomLevel, Frame frame) {
        addressMap.clear();
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("status")) {
            String status = jsonObject.getString("status");
            switch (status) {
                case "OK":
                    JSONArray resultsArr = jsonObject.getJSONArray("results");
                    for (int i = 0; i < resultsArr.length(); i++) {
                        AddressClassForGoogle addressClass = new AddressClassForGoogle();

                        JSONArray componentsArray = resultsArr.getJSONObject(i).getJSONArray("address_components");
                        for (int j = componentsArray.length() - 1; j >= 0; j--) {
                            JSONObject component = componentsArray.getJSONObject(j);
                            JSONArray types = component.getJSONArray("types");
                            for (int k = 0; k < types.length(); k++) {
                                switch (types.getString(k)) {
                                    case "country":
                                        addressClass.setCountry(component.getString("long_name"));
                                        break;
                                    case "locality":
                                        if (!zoomLevel.equals(COUNTRY_LEVEL)) {
                                            addressClass.setCity(component.getString("long_name"));
                                        }
                                        break;
                                    case "route":
                                        if (zoomLevel.equals(STREET_LEVEL) || zoomLevel.equals(BUILDING_LEVEL)) {
                                            addressClass.setStreet(component.getString("long_name"));
                                        }
                                        break;
                                    case "street_number":
                                        if (zoomLevel.equals(BUILDING_LEVEL)) {
                                            addressClass.setBuilding(component.getString("long_name"));
                                        }
                                        break;
                                }
                            }
                        }
                        addressClass.buildFullAddress();
                        addressList.add(addressClass.getAddress());

                        JSONObject locationObj = resultsArr.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                        addressClass.setLat(locationObj.getDouble("lat"));
                        addressClass.setLon(locationObj.getDouble("lng"));

                        addressMap.put(addressClass.getAddress(), addressClass);
                    }
                    break;
                case "OVER_QUERY_LIMIT":
                    frame.showNotification(messages.getMessage(messages.getMainMessagePack(), "message.queryLimit"), Frame.NotificationType.HUMANIZED);
                    log.error(json);
                    break;
                case "ZERO_RESULTS":
                    frame.showNotification(messages.getMessage(messages.getMainMessagePack(), "message.addressNotFound"), Frame.NotificationType.HUMANIZED);
                    log.info(json);
                    break;
                default:
                    frame.showNotification(messages.getMessage(messages.getMainMessagePack(), "message.queryError"), Frame.NotificationType.WARNING);
                    log.info(json);
                    break;
            }
        }
    }

    public void getAddressSearchResult(SuggestionField field, String currentSearchString, Frame frame) {
        List<String> resultArray = getAddressFromGoogle(currentSearchString, frame);
        if (resultArray.isEmpty()) {
            resultArray.add("");
        }
        field.showSuggestions(resultArray);
    }

    public AddressClassForGoogle getAddressClass(String address) {
        AddressClassForGoogle result = addressMap.get(address);
        if (result == null) {
            result = new AddressClassForGoogle();
        }
        return result;
    }

    public void pickAddressByMap(MapPicker mapPicker, SuggestionField field, AddressClassForGoogle addressField) {
        mapPicker.addCloseListener(actionId -> {
            String address = mapPicker.getAddress();
            if (address != null ) {
                addressField.fillAddressClassForGoogle(getAddressClass(address));
                field.setValue(addressField.getAddress());
            }
        });
    }

    public String findAddressByCoordinates(Double lat, Double lng, MapAssistant.ZoomLevel zoomLevel, Frame frame) {
        String result = geocodingService.findAddressByCoordinates(lat, lng);
        List<String> addressList = new ArrayList<>();
        parseAddressJson(result, addressList, zoomLevel, frame);
        if (!addressList.isEmpty()) {
            return addressList.get(0);
        } else {
            return null;
        }
    }
}
