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
}
