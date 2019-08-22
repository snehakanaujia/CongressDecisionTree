
public class DecisionTree<T> {

	private T data;
	private DecisionTree<T> left;
	private DecisionTree<T> right;

	DecisionTree (T data) {
		this.data = data;
		right = null;
		left = null;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setLeftChild(DecisionTree<T> left) {
		this.left = left;
	}

	public DecisionTree<T> getLeftChild() {
		return left;
	}

	public void setRightChild(DecisionTree<T> right ) {
		this.right = right;
	}

	public DecisionTree<T> getRightChild() {
		return right;
	}

	public boolean hasRightChild() {
		if(right != null) {
			return true;
		}
		return false;
	}

	public boolean hasLeftChild() {
		if(left != null) {
			return true;
		}
		return false;
	}

	public boolean isLeaf() {
		if(left == null && right == null) {
			return true;
		}
		return false;
	}
	
	public void output(int tab) {
		if(!isLeaf()) {
			if(getData() != null) {
				System.out.println(getData());
			}
			tab++;
			if(hasLeftChild() && !getLeftChild().isLeaf()) {
				for(int i = 0; i < tab; i++) {
					System.out.print("\t");
				}
				getLeftChild().output(tab);
			}
			if(hasRightChild() && !getRightChild().isLeaf()) {
				for(int i = 0; i < tab; i++) {
					System.out.print("\t");
				}
				getRightChild().output(tab);
			}
		}
	}
}