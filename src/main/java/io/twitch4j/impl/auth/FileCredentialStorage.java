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

package io.twitch4j.impl.auth;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.twitch4j.IClient;
import io.twitch4j.auth.AbstractCredentialStorage;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.ICredentialStorage;
import io.twitch4j.utils.LoggerType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Slf4j(topic = LoggerType.CREDENTIAL_MANAGER)
public class FileCredentialStorage extends AbstractCredentialStorage implements ICredentialStorage {
	private final Collection<ICredential> credentials = new FileCollection();
	public FileCredentialStorage(IClient client) {
		super(client);
	}

	/**
	 * For each interactions with this collection (i talking about {@link FileCollection#remove(Object)} and {@link FileCollection#add(ICredential)})
	 * will automatically saving into <code>credentials.json</code>
	 * you can made own {@link ICredentialStorage} using this interface.
	 */
	private class FileCollection extends AbstractCollection<ICredential> implements Collection<ICredential> {
		private final File file;
		private final ObjectMapper mapper;

		private final List<ICredential> credentials = new LinkedList<>();

		FileCollection() {
			this.file = new File("credentials.json");
			this.mapper = new ObjectMapper();
			this.mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
			this.mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);

			load();
		}

		@Override
		public Iterator<ICredential> iterator() {
			return credentials.iterator();
		}

		@Override
		public int size() {
			return credentials.size();
		}

		@Override
		public boolean add(ICredential credential) {
			if (credentials.add(credential)) {
				save();
				return true;
			} else return false;
		}

		@Override
		public boolean remove(Object o) {
			if (super.remove(o)) {
				save();
				return true;
			} else return false;
		}

		private void load() {
			try {
				if (file.exists()) {
					Collection<ICredential> credentials = Arrays.asList(mapper.readValue(file, Credential[].class));
					credentials.forEach(credential -> {
						try {
							credentials.add(getClient().getCredentialManager().rebuildCredentialData(credential));
						} catch (Exception e) {
							FileCredentialStorage.log.error(ExceptionUtils.getMessage(e), e);
						}
					});
				}
			} catch (Exception e) {
				FileCredentialStorage.log.error(ExceptionUtils.getMessage(e), e);
			}
		}

		private void save() {
			try {
				if (!file.exists()) {
					file.createNewFile();
				}

				TypeReference<Map<String, ?>> typeRef = new TypeReference<Map<String, ?>>() {};
				Collection<Map<String, ?>> data = this.credentials.stream().map(credential -> {
					Map<String, ?> value = mapper.convertValue(credential, typeRef);
					value.remove("user");
					return value;
				}).collect(Collectors.toList());
				mapper.writeValue(file, data);
			} catch (Exception e) {
				FileCredentialStorage.log.error(ExceptionUtils.getMessage(e), e);
			}
		}
	}
}
