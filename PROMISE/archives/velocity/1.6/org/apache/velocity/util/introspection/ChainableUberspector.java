public interface ChainableUberspector extends Uberspect
{
    /**
     * Specify the decorated Uberspector
     * 
     * @param inner The decorated uberspector.
     */
    public void wrap(Uberspect inner);
}
