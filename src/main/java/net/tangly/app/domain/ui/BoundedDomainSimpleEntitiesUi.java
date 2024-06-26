/*
 * Copyright 2023-2024 Marcel Baumann
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

package net.tangly.app.domain.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import net.tangly.app.domain.model.BoundedDomainSimpleEntities;
import net.tangly.commons.lang.functional.LazyReference;
import net.tangly.core.*;
import net.tangly.core.providers.Provider;
import net.tangly.ui.app.domain.BoundedDomainUi;
import net.tangly.ui.components.EntityForm;
import net.tangly.ui.components.EntityView;
import net.tangly.ui.components.ItemView;
import net.tangly.ui.components.Mode;
import org.jetbrains.annotations.NotNull;

/**
 * Test user interface when the domain entities are modeled with Java record. The first entities are editable, the second entities are displayed as readonly items.
 * <p>The simple entities are displayed using the {@link EntityView}, {@link net.tangly.ui.components.EntityFilter}, and {@link EntityForm}.
 * These visual classes have logic to display all properties of a simple entity.</p>
 */
public class BoundedDomainSimpleEntitiesUi extends BoundedDomainUi<BoundedDomainSimpleEntities> {
    private final LazyReference<EntityOneView> entityOneView;
    private final LazyReference<EntityTwoView> entityTwoView;
    private Component currentView;

    /**
     * Test the edit and readonly modes of the views for an entity.
     *
     * @param domain domain to visualize
     */
    public BoundedDomainSimpleEntitiesUi(BoundedDomainSimpleEntities domain) {
        super(domain);
        entityOneView = new LazyReference<>(() -> new EntityOneView(BoundedDomainSimpleEntities.SimpleEntityOne.class, domain, domain.realm().oneEntities(),
                Mode.VIEW));
        entityTwoView = new LazyReference<>(() -> new EntityTwoView(BoundedDomainSimpleEntities.simpleEntityTwo.class, domain, domain.realm().twoEntities(),
                Mode.EDIT));
        currentView(entityOneView);
    }

    @Override
    public void select(@NotNull AppLayout layout, @NotNull MenuBar menuBar) {
        MenuItem menuItem = menuBar.addItem(ENTITIES);
        SubMenu subMenu = menuItem.getSubMenu();
        subMenu.addItem("Entity One", e -> select(layout, entityOneView));
        subMenu.addItem("Entity Two", e -> select(layout, entityTwoView));
        select(layout);
    }

    @Override
    public void refreshViews() {
        entityOneView.ifPresent(ItemView::refresh);
        entityTwoView.ifPresent(ItemView::refresh);
    }

    static class EntityOneView extends EntityView<BoundedDomainSimpleEntities.SimpleEntityOne> {
        public EntityOneView(@NotNull Class<BoundedDomainSimpleEntities.SimpleEntityOne> entityClass, @NotNull BoundedDomainSimpleEntities domain,
                             @NotNull Provider<BoundedDomainSimpleEntities.SimpleEntityOne> provider, @NotNull Mode mode) {
            super(entityClass, domain, provider, mode);
            form(() -> new EntityOneForm(this));
            initEntityView();
        }

        public static class EntityOneForm extends EntityForm<BoundedDomainSimpleEntities.SimpleEntityOne, EntityOneView> {
            public EntityOneForm(@NotNull EntityOneView parent) {
                super(parent);
                initEntityForm();
            }

            @Override
            protected BoundedDomainSimpleEntities.SimpleEntityOne createOrUpdateInstance(BoundedDomainSimpleEntities.SimpleEntityOne entity) {
                return new BoundedDomainSimpleEntities.SimpleEntityOne(fromBinder(HasOid.OID), fromBinder(HasId.ID), fromBinder(HasName.NAME),
                        DateRange.of(fromBinder(HasDateRange.FROM), fromBinder(HasDateRange.TO)), fromBinder(HasText.TEXT));
            }
        }
    }

    static class EntityTwoView extends EntityView<BoundedDomainSimpleEntities.simpleEntityTwo> {
        public EntityTwoView(@NotNull Class<BoundedDomainSimpleEntities.simpleEntityTwo> entityClass, @NotNull BoundedDomainSimpleEntities domain,
                             @NotNull Provider<BoundedDomainSimpleEntities.simpleEntityTwo> provider, @NotNull Mode mode) {
            super(entityClass, domain, provider, mode);
            form(() -> new EntityTwoForm(this));
            initEntityView();
        }

        public static class EntityTwoForm extends EntityForm<BoundedDomainSimpleEntities.simpleEntityTwo, EntityTwoView> {
            public EntityTwoForm(@NotNull EntityTwoView parent) {
                super(parent);
                initEntityForm();
            }

            @Override
            protected BoundedDomainSimpleEntities.simpleEntityTwo createOrUpdateInstance(BoundedDomainSimpleEntities.simpleEntityTwo entity) {
                return new BoundedDomainSimpleEntities.simpleEntityTwo(oid(), id(), name(), dateRange(), text());
            }

        }
    }
}
