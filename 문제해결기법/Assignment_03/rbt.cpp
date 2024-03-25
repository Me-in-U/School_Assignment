#include <fstream>
#include <iostream>
#include <cstring>

using namespace std;

struct Node
{
	long data;
	Node *parent;
	Node *left;
	Node *right;
	bool color; // true for red, false for black
};

class RedBlackTree
{
public:
	Node *root;
	Node *nullLeaf;

	RedBlackTree()
	{
		nullLeaf = new Node();
		nullLeaf->left = nullLeaf->right = nullLeaf;
		nullLeaf->color = false; // Black
		root = nullLeaf;
	}

	void deleteNode(long key)
	{
		Node *z = searchTree(key);
		Node *x;
		Node *y = z;
		bool yOriginalColor = y->color;

		if (z->left == nullLeaf)
		{
			x = z->right;
			replaceNode(z, z->right);
		}
		else if (z->right == nullLeaf)
		{
			x = z->left;
			replaceNode(z, z->left);
		}
		else
		{
			y = successor(z);
			yOriginalColor = y->color;
			x = y->right;
			if (y->parent == z)
			{
				x->parent = y;
			}
			else
			{
				replaceNode(y, y->right);
				y->right = z->right;
				y->right->parent = y;
			}
			replaceNode(z, y);
			y->left = z->left;
			y->left->parent = y;
			y->color = z->color;
		}
		if (!yOriginalColor)
			deleteFixup(x);
	}

	void deleteFixup(Node *x)
	{
		Node *w;
		while (x != root && !x->color)
		{
			if (x == x->parent->left)
			{
				w = x->parent->right;
				if (w->color)
				{
					w->color = false;
					x->parent->color = true;
					leftRotate(x->parent);
					w = x->parent->right;
				}
				if (!w->left->color && !w->right->color)
				{
					w->color = true;
					x = x->parent;
				}
				else
				{
					if (!w->right->color)
					{
						w->left->color = false;
						w->color = true;
						rightRotate(w);
						w = x->parent->right;
					}
					w->color = x->parent->color;
					x->parent->color = false;
					w->right->color = false;
					leftRotate(x->parent);
					x = root;
				}
			}
			else
			{
				w = x->parent->left;
				if (w->color)
				{
					w->color = false;
					x->parent->color = true;
					rightRotate(x->parent);
					w = x->parent->left;
				}
				if (!w->right->color)
				{
					w->color = true;
					x = x->parent;
				}
				else
				{
					if (!w->left->color)
					{
						w->right->color = false;
						w->color = true;
						leftRotate(w);
						w = x->parent->left;
					}
					w->color = x->parent->color;
					x->parent->color = false;
					w->left->color = false;
					rightRotate(x->parent);
					x = root;
				}
			}
		}
		x->color = false;
	}

	void replaceNode(Node *u, Node *v)
	{
		if (u->parent == nullptr)
			root = v;
		else if (u == u->parent->left)
			u->parent->left = v;
		else
			u->parent->right = v;
		v->parent = u->parent;
	}

	void insert(long key)
	{
		Node *node = new Node();
		node->parent = nullptr;
		node->data = key;
		node->left = nullLeaf;
		node->right = nullLeaf;
		node->color = true; // New nodes are red

		Node *y = nullptr;
		Node *x = this->root;

		while (x != nullLeaf)
		{
			y = x;
			if (node->data < x->data)
				x = x->left;
			else
				x = x->right;
		}

		node->parent = y;
		if (y == nullptr)
			root = node;
		else if (node->data < y->data)
			y->left = node;
		else
			y->right = node;

		if (node->parent == nullptr)
		{
			node->color = false;
			return;
		}

		if (node->parent->parent == nullptr)
			return;

		insertFixup(node);
	}

	void insertFixup(Node *node)
	{
		Node *uncle;
		while (node != root && node->parent->color == true)
		{
			if (node->parent == node->parent->parent->right)
			{
				uncle = node->parent->parent->left; // Uncle node
				if (uncle->color == true)
				{ // Uncle is red
					node->parent->color = false;
					uncle->color = false;
					node->parent->parent->color = true;
					node = node->parent->parent;
				}
				else
				{
					if (node == node->parent->left)
					{
						node = node->parent;
						rightRotate(node);
					}
					node->parent->color = false;
					node->parent->parent->color = true;
					leftRotate(node->parent->parent);
				}
			}
			else
			{
				uncle = node->parent->parent->right; // Uncle node
				if (uncle->color == true)
				{ // Uncle is red
					node->parent->color = false;
					uncle->color = false;
					node->parent->parent->color = true;
					node = node->parent->parent;
				}
				else
				{
					if (node == node->parent->right)
					{
						node = node->parent;
						leftRotate(node);
					}
					node->parent->color = false;
					node->parent->parent->color = true;
					rightRotate(node->parent->parent);
				}
			}
		}
		root->color = false;
	}

	void leftRotate(Node *x)
	{
		Node *y = x->right;
		x->right = y->left;
		if (y->left != nullLeaf)
		{
			y->left->parent = x;
		}
		y->parent = x->parent;
		if (x->parent == nullptr)
		{
			this->root = y;
		}
		else if (x == x->parent->left)
		{
			x->parent->left = y;
		}
		else
		{
			x->parent->right = y;
		}
		y->left = x;
		x->parent = y;
	}

	void rightRotate(Node *x)
	{
		Node *y = x->left;
		x->left = y->right;
		if (y->right != nullLeaf)
		{
			y->right->parent = x;
		}
		y->parent = x->parent;
		if (x->parent == nullptr)
		{
			this->root = y;
		}
		else if (x == x->parent->right)
		{
			x->parent->right = y;
		}
		else
		{
			x->parent->left = y;
		}
		y->right = x;
		x->parent = y;
	}

	Node *searchTree(long key)
	{
		Node *node = root;
		while (node != nullLeaf)
		{
			if (key < node->data)
			{
				node = node->left;
			}
			else if (key > node->data)
			{
				node = node->right;
			}
			else
			{
				return node;
			}
		}
		return nullLeaf;
	}

	Node *successor(Node *x)
	{
		if (x->right != nullLeaf)
		{
			Node *temp = x->right;
			while (temp->left != nullLeaf)
			{
				temp = temp->left;
			}
			return temp;
		}
		Node *temp = x->parent;
		while (temp != nullLeaf && x == temp->right)
		{
			x = temp;
			temp = temp->parent;
		}
		return temp;
	}
};

int main()
{
	ifstream inp("rbt.inp");
	ofstream out("rbt.out");
	char line[100]; // 각 줄을 읽기 위한 문자 배열

	RedBlackTree bst;
	while (inp.getline(line, sizeof(line)))
	{
		char command = line[0];
		long key = atol(line + 2);
		if (key < 0)
			break;
		switch (command)
		{
		case 'i':
			bst.insert(key);
			break;
		case 'c':
		{
			Node *result = bst.searchTree(key);
			string color = result->color ? "RED" : "BLACK";
			out << "color(" << key << "): " << color << '\n';
			break;
		}
		case 'd':
			bst.deleteNode(key);
			break;
		}
	}
	return 0;
}