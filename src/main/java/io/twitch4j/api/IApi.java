/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.twitch4j.api;

import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Map;

public interface IApi {
	default Response get(String url, Map<String, String> headers) throws Exception {
		return request(RequestType.GET, url, headers);
	}
	default Response post(String url, RequestBody body, Map<String, String> headers) throws Exception {
		return request(RequestType.POST, url, body, headers);
	}
	default Response put(String url, RequestBody body, Map<String, String> headers) throws Exception {
		return request(RequestType.PUT, url, body, headers);
	}
	default Response patch(String url, RequestBody body, Map<String, String> headers) throws Exception {
		return request(RequestType.PATCH, url, body, headers);
	}
	default Response delete(String url, Map<String, String> headers) throws Exception {
		return request(RequestType.DELETE, url, headers);
	}

	default Response get(String url) throws Exception {
		return get(url, null);
	}
	default Response post(String url, RequestBody body) throws Exception {
		return post(url, body, null);
	}
	default Response put(String url, RequestBody body) throws Exception {
		return put(url, body, null);
	}
	default Response patch(String url, RequestBody body) throws Exception {
		return patch(url, body, null);
	}
	default Response delete(String url) throws Exception {
		return delete(url, null);
	}

	default Response request(RequestType requestType, String url) throws Exception {
		return request(requestType, url, null);
	}

	default Response request(RequestType requestType, String url, Map<String, String> headers) throws Exception {
		return request(requestType, url, null, headers);
	}

	Response request(RequestType requestType, String url, RequestBody body, Map<String, String> headers) throws Exception;
}
