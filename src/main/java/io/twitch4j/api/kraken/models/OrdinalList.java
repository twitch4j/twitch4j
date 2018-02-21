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

package io.twitch4j.api.kraken.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.twitch4j.api.Model;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdinalList<E> extends Model implements List<E>{
	@JsonAlias({"actions", "users", "teams", "communities", "emoticon_sets", "emoticons", "ingests"})
	private final List<E> data;

	public OrdinalList(List<E> data) { this.data = Collections.unmodifiableList(data); }

	@Override
	public E get(int index) { return data.get(index); }

	@Override
	public E set(int index, E element) { return data.set(index, element); }

	@Override
	public void add(int index, E element) { data.add(index, element); }

	@Override
	public E remove(int index) { return data.remove(index); }

	@Override
	public int indexOf(Object o) { return data.indexOf(o); }

	@Override
	public int lastIndexOf(Object o) { return data.lastIndexOf(o); }

	@Override
	public ListIterator<E> listIterator() { return data.listIterator(); }

	@Override
	public ListIterator<E> listIterator(int index) { return data.listIterator(index); }

	@Override
	public List<E> subList(int fromIndex, int toIndex) { return data.subList(fromIndex, toIndex); }

	@Override
	public void forEach(Consumer<? super E> action) { data.forEach(action); }

	@Override
	public Spliterator<E> spliterator() { return data.spliterator(); }

	@Override
	public Stream<E> stream() { return data.stream(); }

	@Override
	public Stream<E> parallelStream() { return data.parallelStream(); }

	@Override
	public int size() { return data.size(); }

	@Override
	public boolean isEmpty() { return data.isEmpty(); }

	@Override
	public boolean contains(Object o) { return data.contains(o); }

	@Override
	public Iterator<E> iterator() { return data.iterator();	}

	@Override
	public Object[] toArray() { return data.toArray(); }

	@Override
	public <T> T[] toArray(T[] a) { return data.toArray(a); }

	@Override
	public boolean add(E e) { return data.add(e); }

	@Override
	public boolean remove(Object o) { return data.remove(o); }

	@Override
	public boolean containsAll(Collection<?> c) { return data.containsAll(c); }

	@Override
	public boolean addAll(Collection<? extends E> c) { return data.addAll(c); }

	@Override
	public boolean addAll(int index, Collection<? extends E> c) { return data.addAll(index, c); }

	@Override
	public boolean removeAll(Collection<?> c) { return data.removeAll(c); }

	@Override
	public boolean retainAll(Collection<?> c) { return data.retainAll(c); }

	@Override
	public boolean removeIf(Predicate<? super E> filter) { return data.removeIf(filter); }

	@Override
	public void replaceAll(UnaryOperator<E> operator) { data.replaceAll(operator); }

	@Override
	public void sort(Comparator<? super E> c) { data.sort(c); }

	@Override
	public void clear() { data.clear(); }
}
