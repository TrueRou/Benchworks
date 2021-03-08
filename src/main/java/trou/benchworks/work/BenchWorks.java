package trou.benchworks.work;

public enum BenchWorks {
    SHEARING(new WorkShearing()),
    DIGGING(new WorkDigging()),
    ATTACKING(new WorkAttacking()),
    PLACING(new WorkPlacing()),
    BREEDING(new WorkBreeding()),
    MILKING(new WorkMilking());

    public IBenchWork benchWork;

    BenchWorks(IBenchWork benchWork) {
        this.benchWork = benchWork;
    }
}
