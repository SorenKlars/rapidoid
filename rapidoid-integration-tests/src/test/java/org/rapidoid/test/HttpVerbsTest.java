package org.rapidoid.test;

/*
 * #%L
 * rapidoid-integration-tests
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.http.HTTP;
import org.rapidoid.http.fast.On;

@Authors("Nikolche Mihajlovski")
@Since("5.0.5")
public class HttpVerbsTest extends TestCommons {

	@Test
	public void testHttpVerbs() {
		On.get("/testGet").plain("get:success");
		On.post("/testPost").plain("post:success");
		On.put("/testPut").plain("put:success");
		On.delete("/testDelete").plain("delete:success");
		On.patch("/testPatch").plain("patch:success");
		On.options("/testOptions").plain("options:success");
		On.head("/testHead").plain(""); // no body for the HEAD verb
		On.trace("/testTrace").plain("trace:success");

		eq(new String(HTTP.get("http://localhost:8888/testGet")), "get:success");
		eq(new String(HTTP.post("http://localhost:8888/testPost")), "post:success");
		eq(new String(HTTP.put("http://localhost:8888/testPut")), "put:success");
		eq(new String(HTTP.delete("http://localhost:8888/testDelete")), "delete:success");
		eq(new String(HTTP.patch("http://localhost:8888/testPatch")), "patch:success");
		eq(new String(HTTP.options("http://localhost:8888/testOptions")), "options:success");
		eq(new String(HTTP.head("http://localhost:8888/testHead")), ""); // no body for the HEAD verb
		eq(new String(HTTP.trace("http://localhost:8888/testTrace")), "trace:success");

		On.getDefaultSetup().shutdown();
	}

}
