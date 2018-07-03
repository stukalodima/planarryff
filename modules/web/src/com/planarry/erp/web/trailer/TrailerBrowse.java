package com.planarry.erp.web.trailer;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.planarry.erp.entity.AccessToTrailer;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.Trailer;
import com.planarry.erp.web.trailer.access.TrailerAccessible;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TrailerBrowse extends AbstractLookup {

    @Inject
    private GroupDatasource<Trailer, UUID> trailersDs;

    @Named("shareToAllBtn")
    private Button shareToAllBtn;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(DataManager.NAME)
    private DataManager dataManager;


    @Override
    public void init(Map<String, Object> params) {
        if (controllerAssistant.checkUserStatusAndCompany(params)) {
            showNotification(messages.getMessage(getClass(), "message.cantLoadTrailers"), NotificationType.WARNING);
        }
        addTrailerDsItemChangeListener();
        super.init(params);
    }

    private void addTrailerDsItemChangeListener() {
        trailersDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                defineShareBtnCaption();
            }
        });
    }

    public void onShareToAll() {
        Trailer trailer = trailersDs.getItem();
        clearAccessDS(trailer);
        trailer.setAccessibleToAll(!trailer.getAccessibleToAll());
        trailer.setAccessibleToOwner(!trailer.getAccessibleToAll());
        defineShareBtnCaption();
        dataManager.commit(trailer);
        trailersDs.refresh();
    }

    public void onShareToCompanies() {
        Trailer trailer = dataManager.reload(trailersDs.getItem(), "trailer-view");
        List<AccessToTrailer> accesses = new ArrayList<>(trailer.getAccesses());
        List<Company> companies = accesses.stream().map(AccessToTrailer::getCompany).collect(Collectors.toList());
        Map<String, Object> params = ParamsMap.of("trailer", trailer, "accesses", accesses, "companies", companies);
        TrailerAccessible window = (TrailerAccessible) openWindow("trailerAccessible", WindowManager.OpenType.DIALOG, params);
        window.addCloseWithCommitListener(() -> {
            List<AccessToTrailer> accessToCargoes = trailer.getAccesses();
            CommitContext commitContext = new CommitContext();
            accessToCargoes.forEach(item -> {
                if (!window.getNewAccesses().contains(item)) {
                    commitContext.addInstanceToRemove(item);
                    accesses.remove(item);
                }
            });

            window.getNewAccesses().forEach(item -> {
                if (accesses.stream().noneMatch(accessToTrailer -> accessToTrailer.getCompany().equals(item.getCompany()))) {
                    commitContext.addInstanceToCommit(item);
                    accesses.add(item);
                }
            });

            trailer.setAccessibleToOwner(accesses.size() == 0);
            trailer.setAccessibleToAll(false);
            defineShareBtnCaption();

            commitContext.addInstanceToCommit(trailer);
            dataManager.commit(commitContext);
            trailersDs.refresh();
        });
    }

    private void defineShareBtnCaption() {
        String captionMsg = trailersDs.getItem().getAccessibleToAll() ? "unshare" : "shareToAll";
        shareToAllBtn.setCaption(messages.getMessage(getClass(), captionMsg));
    }

    private void clearAccessDS(Trailer trailer) {
        trailer = dataManager.reload(trailer, "trailer-view");

        if (trailer.getAccesses().size() > 0) {
            CommitContext commitContext = new CommitContext();
            trailer.getAccesses().forEach(commitContext::addInstanceToRemove);
            dataManager.commit(commitContext);
        }
    }

    public Component generateAccessColumn(Entity entity) {
        Trailer trailer = (Trailer) entity;
        String ownerMessage = messages.getMessage(getClass(), "ownerAccessMessageFull_1") + " " + trailer.getCompany().getName()
                + messages.getMessage(getClass(), "ownerAccessMessageFull_2");
        return controllerAssistant.generateAccessColumn(trailer.getAccessibleToOwner(), trailer.getAccessibleToAll(), trailer.getAccesses(), getFrame(),
                ownerMessage, messages.getMessage(getClass(), "allAccessMessageFull"));
    }

}
