class Renshu {

    // xを2倍にして返す関数
    public int doubleValue(int x) {
        return x * 2;
    }

    //ここに続きを実装していく。

    public int sumUpToN(int n){
        int sum=0;
        for(int i=1; i<=n; i++){
            sum=sum+i;
        }
        return sum;
    }

    public int sumFromPtoQ(int p, int q){
        int sum1=0;
        if(p>q){
            return -1;
        }
        for(int i=p; i<=q; i++){
            sum1=sum1+i;
        }
        return sum1;
    }

    public int sumFromArrayIndex(int[] a, int index){
        int sum2=0;
        if(index>=a.length){
            return -1;
        }
        for(int i=index; i<a.length; i++){
            sum2=sum2+a[i];
        }
        return sum2;
    }

    public int selectMaxValue(int[] a){
        int max=a[0];
        for(int i=0; i<a.length; i++){
            if(max<a[i]){
                max=a[i];
            }
    }
    return max;
}
    public int selectMinValue(int[] a){
    int min=a[0];
    for(int i=0; i<a.length; i++){
        if(min>a[i]){
            min=a[i];
        }
}
return min;
}
    public int selectMaxIndex(int[] a){
        int maxindex = 0;
        int maxValue = a[0];
        for(int i=0; i<a.length; i++){
            if(a[i]>maxValue){
            maxValue=a[i];
            maxindex=i;
            }
        }
        return maxindex;
}
    public int selectMinIndex(int[] a){
    int minindex = 0;
    int minValue = a[0];
    for(int i=0; i<a.length; i++){
        if(a[i]<minValue){
        minValue=a[i];
        minindex=i;
        }
    }
    return minindex;
}
    public void swapArrayElements(int[] p, int i, int j) {
        int temp = p[i];
        p[i] = p[j];
        p[j] = temp;
}
    public boolean swapTwoArrays(int[] a, int[] b) {
    if (a.length != b.length) {
        return false;
    }
    for(int i=0; i<a.length; i++){
    int temp = a[i];
    a[i] = b[i];
    b[i] = temp;
    }
    return true;
}
public void bubble_sort(int[] d) {
    // iはi回目の交換する回数
    for (int i = d.length-1; i > 0; i-- ) {
        // j は交換する箇所の前からの番号を示している
        for (int j = 0; j < i; j++) {
            if(d[j]>d[j+1]){
              //降順にしたい場合は不等号を逆に
            int box = d[j];
            d[j] = d[j+1];
            d[j+1] = box;
            System.out.println(d[j] + ":" +d[j+1]);
            } else{
              //そのまま
            }
        }
    }
}
public static void quickSort(int[] arr) {
    if (arr == null || arr.length == 0) {
        return;
    }
    quickSort(arr, 0, arr.length - 1);
}

private static void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

private static int partition(int[] arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (arr[j] <= pivot) {
            i++;
            swap(arr, i, j);
        }
    }
    swap(arr, i + 1, high);
    return i + 1;
}

private static void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}
}

