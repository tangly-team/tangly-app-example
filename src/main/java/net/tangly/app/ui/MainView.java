/*
 * Copyright 2006-2024 Marcel Baumann
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *          https://apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */

package net.tangly.app.ui;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.tangly.app.Application;
import net.tangly.app.ApplicationView;
import net.tangly.app.domain.model.BoundedDomainEntities;
import net.tangly.app.domain.model.BoundedDomainSimpleEntities;
import net.tangly.app.domain.rest.BoundedDomainSimpleEntitiesPortRest;
import net.tangly.app.domain.ui.BoundedDomainEntitiesUi;
import net.tangly.app.domain.ui.BoundedDomainSimpleEntitiesUi;

@PageTitle("tangly Vaadin UI")
@Route("")
public class MainView extends ApplicationView {
    private static final String IMAGE_NAME = "tangly70x70.png";

    public MainView() {
        var application = Application.instance();

        var domainSimpleEntities = BoundedDomainSimpleEntities.create();
        application.registerBoundedDomain(domainSimpleEntities);
        application.registerBoundedDomainRest(new BoundedDomainSimpleEntitiesPortRest(domainSimpleEntities));

        var domainEntities = BoundedDomainEntities.create();
        application.registerBoundedDomain(BoundedDomainEntities.create());

        super(IMAGE_NAME);
        registerBoundedDomainUi(new BoundedDomainSimpleEntitiesUi(domainSimpleEntities));
        registerBoundedDomainUi(new BoundedDomainEntitiesUi(domainEntities));
        drawerMenu();
        selectBoundedDomainUi(BoundedDomainEntities.DOMAIN);
    }
}
