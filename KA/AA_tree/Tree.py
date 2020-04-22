class Node(object):
    def __init__(self, value, level=1, left=None, right=None):
        self.value = value
        self.level = level
        self.left = left
        self.right = right


class Tree(object):
    def __init__(self):
        self.root = None

    # Устранение левой связи на одном уровне
    def _skew(self, t):
        if t is None:
            return None
        elif t.left is None:
            return t
        elif t.left.level == t.level:

            l = t.left
            l, t = t, l
            l.left = t.right
            t.right = l

        return t

    # Устранение двух правых связей на одном уровне
    def _split(self, t):
        if t is None:
            return None
        elif t.right is None or t.right.right is None:
            return t
        elif t.level == t.right.right.level:

            r = t.right
            t, r = r, t
            r.right = t.left
            t.left = r
            t.level += 1

        return t

    def _insert(self, value, t):
        if t is None:
            return Node(value)
        else:
            if value < t.value:
                t.left = self._insert(value, t.left)
            elif value > t.value:
                t.right = self._insert(value, t.right)
        t = self._split(self._skew(t))
        return t

    def insert(self, value):
        self.root = self._insert(value, self.root)

    def _print_tree(self, node, lvl):
        if node is not None:
            self._print_tree(node.right, lvl)
            print("    " * (lvl - node.level), node.value)
            self._print_tree(node.left, lvl)

    def print_tree(self):
        self._print_tree(self.root, self.root.level)
        print()
        print()

    def _print_LNR(self, node):
        if node is not None:
            self._print_LNR(node.left)
            print(str(node.value))
            self._print_LNR(node.right)

    def print_LNR(self):
        self._print_LNR(self.root)
        print()

    def _search(self, node, x):
        if node is None:
            return None
        elif x == node.value:
            return x
        elif x < node.value:
            return self._search(node.left, x)
        else:
            return self._search(node.right, x)

    def search(self, x):
        element = self._search(self.root, x)
        if element is not None:
            print('Элемент', x, 'присутствует в дереве.')
        else:
            print('Элемента', x, 'в дереве нет.')

    # Обновляем уровень вершины, в зависимости от значения уровня дочерних вершин
    def _decrease_level(self, t):
        if t.left is not None and t.right is not None:
            should_be = min(t.left.level, t.right.level) + 1
        else:
            should_be = 1

        if should_be < t.level:
            t.level = should_be
            if t.right is not None and should_be < t.right.level:
                t.right.level = should_be
        return t

    # Преемник - самый левый в правом поддереве
    def _successor(self, t):
        t = t.right
        if t.left is None:
            return t
        else:
            while t.left:
                t = t.left
            return t

    # Предшественник - самый правый в левом поддереве
    def _predecessor(self, t):
        t = t.left
        if t.right is None:
            return t
        else:
            while t.right:
                t = t.right
            return t

    def delete(self, value):
        self.root = self._delete(value, self.root)

    def _delete(self, value, t):
        if t is None:
            return t

        elif value > t.value:
            t.right = self._delete(value, t.right)
        elif value < t.value:
            t.left = self._delete(value, t.left)

        else:
            if t.left is None and t.right is None and t.level == 1:
                return None
            elif t.left is None:
                l = self._successor(t)
                t.right = self._delete(l.value, t.right)
                t.value = l.value
            else:
                l = self._predecessor(t)
                t.left = self._delete(l.value, t.left)
                t.value = l.value

        t = self._decrease_level(t)
        t = self._skew(t)
        t.right = self._skew(t.right)
        if t.right is not None:
            t.right.right = self._skew(t.right.right)

        t = self._split(t)
        t.right = self._split(t.right)
        return t


my_tree = Tree()

my_tree.insert(4)
my_tree.insert(10)
my_tree.insert(2)
my_tree.insert(6)
my_tree.insert(12)
my_tree.insert(3)
my_tree.insert(1)
my_tree.insert(8)
my_tree.insert(13)
my_tree.insert(11)
my_tree.insert(5)
my_tree.insert(9)
my_tree.insert(7)

my_tree.print_LNR()
my_tree.print_tree()
my_tree.search(8)
my_tree.search(17)

my_tree.delete(1)
my_tree.print_tree()

my_tree.delete(5)
my_tree.print_tree()
