/*
 * Copyright 2024 Marcel Baumann
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

package net.tangly.app.domain.rest;

import com.vaadin.base.devserver.themeeditor.messages.ErrorResponse;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.*;
import net.tangly.app.api.BoundedDomainRest;
import net.tangly.app.domain.model.BoundedDomainSimpleEntities;
import net.tangly.core.providers.Provider;

public class BoundedDomainSimpleEntitiesPortRest implements BoundedDomainRest {
    static final String PREFIX = STR."/rest/\{BoundedDomainSimpleEntities.DOMAIN.toLowerCase()}/entities-ones";

    BoundedDomainSimpleEntities domain;

    public BoundedDomainSimpleEntitiesPortRest(BoundedDomainSimpleEntities domain) {
        this.domain = domain;
    }

    @Override
    public String name() {
        return domain.name();
    }

    @Override
    public void registerEndPoints(Javalin javalin) {
        javalin.get(PREFIX, this::getAll);
        javalin.get(STR."\{PREFIX}/{id}", this::getById);
        javalin.put(PREFIX, this::create);
        javalin.patch(STR."\{PREFIX}/{id}", this::update);
        javalin.delete(STR."\{PREFIX}/{id}", this::delete);
    }

    @OpenApi(
        summary = "Get all entities",
        operationId = "getAllEntitiesOne",
        path = "/entities-ones",
        methods = HttpMethod.GET,
        tags = {"SimpleEntityOne"},
        responses = {
            @OpenApiResponse(status = "200", content = {@OpenApiContent(from = BoundedDomainSimpleEntities.SimpleEntityOne[].class)})
        }
    )
    private void getAll(Context ctx) {
        ctx.json(domain.realm().oneEntities().items());
    }

    @OpenApi(
        summary = "Get an entity by id",
        operationId = "getEntityOneById",
        path = "/entities-ones/:id",
        methods = HttpMethod.GET,
        tags = {"SimpleEntityOne"},
        pathParams = {
            @OpenApiParam(name = "id",  required = true, type = String.class, description = "The entity identifier")
        },
        responses = {
            @OpenApiResponse(status = "200", content = {@OpenApiContent(from = BoundedDomainSimpleEntities.SimpleEntityOne.class)})
        }
    )
    private void getById(Context ctx) {
        String id = ctx.pathParam("id");
        Provider.findById(oneEntities(), id).ifPresentOrElse(entity -> ctx.json(entity), () -> ctx.status(404));
    }

    @OpenApi(
        summary = "Create entity",
        operationId = "createSimpleEntityOne",
        path = "/simple-entities",
        methods = HttpMethod.POST,
        tags = {"SimpleEntityOne"},
        requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = BoundedDomainSimpleEntities.SimpleEntityOne.class)}),
        responses = {
            @OpenApiResponse(status = "204"),
            @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
            @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
        }
    )
    private void create(Context ctx) {
        BoundedDomainSimpleEntities.SimpleEntityOne updated = ctx.bodyAsClass(BoundedDomainSimpleEntities.SimpleEntityOne.class);
        oneEntities().update(updated);
    }

    @OpenApi(
        summary = "Update an entity identified by ID",
        operationId = "updateUserById",
        path = "/simple-entities/:id",
        methods = HttpMethod.PATCH,
        pathParams = {
            @OpenApiParam(name = "id",  required = true, type = String.class, description = "The entity identifier")
        },
        tags = {"SimpleEntityOne"},
        requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = BoundedDomainSimpleEntities.SimpleEntityOne.class)}),
        responses = {
            @OpenApiResponse(status = "204"),
            @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
            @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
        }
    )
    private void update(Context ctx) {
        String id = ctx.pathParam("id");
        BoundedDomainSimpleEntities.SimpleEntityOne updated = ctx.bodyAsClass(BoundedDomainSimpleEntities.SimpleEntityOne.class);
        Provider.findById(oneEntities(), id).ifPresentOrElse(entity -> oneEntities().update(updated), () -> ctx.status(404));
    }

    @OpenApi(
        summary = "delete entity by ID",
        operationId = "updateUserById",
        path = "/simple-entities/:id",
        methods = HttpMethod.DELETE,
        pathParams = {
            @OpenApiParam(name = "id",  required = true, type = String.class, description = "The entity identifier")
        },
        tags = {"SimpleEntityOne"},
        responses = {
            @OpenApiResponse(status = "204"),
            @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
            @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
        }
    )
    private void delete(Context ctx) {
        String id = ctx.pathParam("id");
        Provider.findById(oneEntities(), id).ifPresentOrElse(entity -> oneEntities().delete(entity), () -> ctx.status(404));
    }

    private Provider<BoundedDomainSimpleEntities.SimpleEntityOne> oneEntities() {
        return domain.realm().oneEntities();
    }
}
