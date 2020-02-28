import hlt.MapCell;

// Stolen (and slightly updated) from
// https://www.geeksforgeeks.org/max-heap-in-java/
public class MaxHeap {
    private MapCell[] Heap;
    private int size;
    private int maxsize;

    // Constructor to initialize an 
    // empty max heap with given maximum 
    // capacity. 
    public MaxHeap(int maxsize, MapCell cell)
    {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new MapCell[this.maxsize + 1];
        Heap[0] = cell;
    }

    // Returns position of parent 
    private int parent(int pos)
    {
        return pos / 2;
    }

    // Below two functions return left and 
    // right children. 
    private int leftChild(int pos) {
        return (2 * pos);
    }

    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }

    // Returns true of given node is leaf 
    private boolean isLeaf(int pos)
    {
        if (pos >= (size / 2) && pos <= size) {
            return true;
        }
        return false;
    }

    private void swap(int fpos, int spos)
    {
        MapCell tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }

    // A recursive function to max heapify the given 
    // subtree. This function assumes that the left and 
    // right subtrees are already heapified, we only need 
    // to fix the root. 
    private void maxHeapify(int pos)
    {
        if (isLeaf(pos))
            return;

        if (Heap[pos].halite < Heap[leftChild(pos)].halite ||
            Heap[pos].halite < Heap[rightChild(pos)].halite) {

            if (Heap[leftChild(pos)].halite > Heap[rightChild(pos)].halite) {
                swap(pos, leftChild(pos));
                maxHeapify(leftChild(pos));
            }
            else {
                swap(pos, rightChild(pos));
                maxHeapify(rightChild(pos));
            }
        }
    }

    // Inserts a new element to max heap 
    public void insert(MapCell element)
    {
        Heap[++size] = element;

        // Traverse up and fix violated property 
        int current = size;
        while (Heap[current].halite > Heap[parent(current)].halite) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public void print()
    {
        for (int i = 1; i <= size / 2; i++) {
            System.out.print(" PARENT : " + Heap[i] + " LEFT CHILD : " +
                Heap[2 * i] + " RIGHT CHILD :" + Heap[2 * i + 1]);
            System.out.println();
        }
    }

    // Remove an element from max heap 
    public MapCell extractMax()
    {
        MapCell popped = Heap[1];
        Heap[1] = Heap[size--];
        maxHeapify(1);
        return popped;
    }
} 
