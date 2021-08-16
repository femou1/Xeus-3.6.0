/*
 * Copyright (c) 2018.
 *
 * This file is part of AvaIre.
 *
 * AvaIre is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AvaIre is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AvaIre.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.avairebot.scheduler.jobs;

import com.avairebot.AvaIre;
import com.avairebot.cache.CacheType;
import com.avairebot.contracts.scheduler.Job;
import com.avairebot.factories.RequestFactory;
import com.avairebot.requests.Response;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GithubChangesJob extends Job {

    private final String cacheToken = "gitlab.commits";

    public GithubChangesJob(AvaIre avaire) {
        super(avaire, 90, 90, TimeUnit.MINUTES);

        if (!avaire.getCache().getAdapter(CacheType.FILE).has(cacheToken)) {
            run();
        }
    }

    @Override
    public void run() {
        handleTask(avaire -> {
            RequestFactory.makeGET("https://gitlab.com/api/v4/projects/17658373/repository/commits")
                .send((Consumer<Response>) response -> {
                    List service = (List) response.toService(List.class);

                    avaire.getCache().getAdapter(CacheType.FILE).forever(cacheToken, service);
                });
        });
    }
}
