/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Contains the all the cards in the deck.
 * <p>
 *
 * @author HW
 */
public class Deck extends ArrayList<AbstractCard> {

	private class Itr implements Iterator<AbstractCard> {

		private final Iterator<AbstractCard> implement;

		Itr(Iterator<AbstractCard> implement) {
			this.implement = implement;
		}

		@Override
		public boolean hasNext() {
			return implement.hasNext();
		}

		@Override
		public AbstractCard next() {
			return implement.next();
		}

		@Override
		public void remove() {
			checkCreation();
			implement.remove();
		}
	}

	private class ListItr extends Itr implements ListIterator<AbstractCard> {

		private final ListIterator<AbstractCard> implement;

		ListItr(ListIterator<AbstractCard> implement) {
			super(implement);
			this.implement = implement;
		}

		@Override
		public void add(AbstractCard e) {
			checkCreation();
			implement.add(e);
		}

		@Override
		public boolean hasNext() {
			return implement.hasNext();
		}

		@Override
		public boolean hasPrevious() {
			return implement.hasPrevious();
		}

		@Override
		public AbstractCard next() {
			return implement.next();
		}

		@Override
		public int nextIndex() {
			return implement.nextIndex();
		}

		@Override
		public AbstractCard previous() {
			return implement.previous();
		}

		@Override
		public int previousIndex() {
			return implement.previousIndex();
		}

		@Override
		public void remove() {
			checkCreation();
			implement.remove();
		}

		@Override
		public void set(AbstractCard e) {
			checkCreation();
			implement.set(e);
		}
	}

	private class SubList extends AbstractList<AbstractCard>implements RandomAccess {

		private final List<AbstractCard> implement;

		SubList(List<AbstractCard> implement) {
			this.implement = implement;
		}

		@Override
		public void add(int index, AbstractCard element) {
			checkCreation();
			implement.add(index, element);
		}

		@Override
		public boolean addAll(Collection<? extends AbstractCard> c) {
			return implement.addAll(c);
		}

		@Override
		public boolean addAll(int index, Collection<? extends AbstractCard> c) {
			return implement.addAll(index, c);
		}

		@Override
		public AbstractCard get(int index) {
			return implement.get(index);
		}

		@Override
		public Iterator<AbstractCard> iterator() {
			return new Itr(implement.iterator());
		}

		@Override
		public ListIterator<AbstractCard> listIterator() {
			return new ListItr(implement.listIterator());
		}

		@Override
		public ListIterator<AbstractCard> listIterator(int index) {
			return new ListItr(implement.listIterator(index));
		}

		@Override
		public AbstractCard remove(int index) {
			return implement.remove(index);
		}

		@Override
		public boolean remove(Object o) {
			return implement.remove(o);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return implement.removeAll(c);
		}

		@Override
		public AbstractCard set(int index, AbstractCard element) {
			checkCreation();
			return implement.set(index, element);
		}

		@Override
		public int size() {
			return implement.size();
		}

		@Override
		public Spliterator<AbstractCard> spliterator() {
			return implement.spliterator();
		}

		@Override
		public List<AbstractCard> subList(int fromIndex, int toIndex) {
			return new SubList(implement.subList(fromIndex, toIndex));
		}
	}

	private static final long serialVersionUID = 1086527244708499003L;

	public static void shuffleDeck(ArrayList<AbstractCard> deck) {
		for (int i = 0; i < deck.size() * 5; i++) {
			int index1 = (int) (Math.random() * deck.size());
			int index2 = (int) (Math.random() * deck.size());
			deck.set(index1, deck.set(index2, deck.get(index1)));
		}
	}

	private volatile boolean creationMode = true;

	private int nextId = 0;

	@Override
	public boolean add(AbstractCard e) {
		checkCreation();
		regId(e);
		return super.add(e);
	}

	@Override
	public void add(int index, AbstractCard element) {
		checkCreation();
		regId(element);
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends AbstractCard> c) {
		checkCreation();
		c.forEach(this::regId);
		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends AbstractCard> c) {
		checkCreation();
		c.forEach(this::regId);
		return super.addAll(index, c);
	}

	/**
	 * Checks whether if this is in creation mode, if not, then throws exception.
	 * <p>
	 *
	 * @throws IllegalStateException
	 *             when this is not in creation mode.
	 */
	public void checkCreation() {
		if (!creationMode) {
			throw new IllegalStateException("not in creation mode.");
		}
	}

	@Override
	public void clear() {
		checkCreation();
		super.clear();
	}

	@Override
	public void ensureCapacity(int minCapacity) {
		checkCreation();
		super.ensureCapacity(minCapacity);
	}

	/**
	 * Finalizes the deck and turns it into non-creation mode.
	 * <p>
	 *
	 * @see #isCreationMode()
	 */
	public void finalizeDeck() {
		if (creationMode) {
			trimToSize();
			creationMode = false;
		}
	}

	/**
	 * Specifies whether if this deck is in creation mode. In creation mode, you can add more cards to this deck, or remove them. Afterwards, no more cards may be added to or removed from.
	 * <p>
	 *
	 * @return true if in creation, false otherwise.
	 */
	public boolean isCreationMode() {
		return creationMode;
	}

	@Override
	public Iterator<AbstractCard> iterator() {
		return new Itr(super.iterator());
	}

	@Override
	public ListIterator<AbstractCard> listIterator() {
		return new ListItr(super.listIterator());
	}

	@Override
	public ListIterator<AbstractCard> listIterator(int index) {
		return new ListItr(super.listIterator(index));
	}

	@Override
	public AbstractCard remove(int index) {
		checkCreation();
		AbstractCard prev = super.remove(index);
		prev.id = -1;
		return prev;
	}

	@Override
	public boolean remove(Object o) {
		checkCreation();
		boolean removed = super.remove(o);
		if (removed) {
			((AbstractCard) o).id = -1;
		}
		return removed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		checkCreation();
		return super.removeAll(c);
	}

	@Override
	public boolean removeIf(Predicate<? super AbstractCard> filter) {
		checkCreation();
		return super.removeIf(filter);
	}

	@Override
	public void replaceAll(UnaryOperator<AbstractCard> operator) {
		checkCreation();
		super.replaceAll(operator);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		checkCreation();
		return super.retainAll(c);
	}

	@Override
	public AbstractCard set(int index, AbstractCard element) {
		checkCreation();
		AbstractCard prev = super.set(index, element);
		if (!Objects.equals(prev, element)) {
			prev.id = -1;
			regId(element);
		}
		return prev;
	}

	public void shuffle() {
		for (int i = 0; i < size() * 5; i++) {
			int index1 = (int) (Math.random() * size());
			int index2 = (int) (Math.random() * size());
			super.set(index1, super.set(index2, super.get(index1)));
		}
	}

	@Override
	public List<AbstractCard> subList(int fromIndex, int toIndex) {
		return new SubList(super.subList(fromIndex, toIndex));
	}

	@Override
	public void trimToSize() {
		checkCreation();
		super.trimToSize();
	}

	private void regId(AbstractCard c) {
		if (c.id >= 0) {
			throw new IllegalArgumentException("AbstractCard has already been added to a deck.");
		} else {
			c.id = nextId++;
		}
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		checkCreation();
		super.removeRange(fromIndex, toIndex);
	}

}
