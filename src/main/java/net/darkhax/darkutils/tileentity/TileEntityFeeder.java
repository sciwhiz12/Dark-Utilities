package net.darkhax.darkutils.tileentity;

import java.util.List;

import net.darkhax.bookshelf.lib.util.EntityUtils;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.tileentity.TileEntityBasic;
import net.darkhax.darkutils.handler.ContentHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

public class TileEntityFeeder extends TileEntityBasic implements IInventory {
    
    private int food = 0;
    
    public int addFood (ItemStack foodStack) {
        
        if (this.food == getSizeInventory())
            return foodStack.stackSize;
            
        setFood(this.food += foodStack.stackSize);
        
        if (this.food > getSizeInventory()) {
            
            int remaining = this.food - getSizeInventory();
            setFood(getSizeInventory());
            return remaining;
        }
        
        return 0;
    }
    
    public int getFood () {
        
        return this.food;
    }
    
    public void setFood (int food) {
        
        this.food = food;
        this.worldObj.setBlockState(this.pos, getStateFromFood());
        this.markDirty();
    }
    
    @Override
    public void onEntityUpdate () {
        
        if (!this.worldObj.isRemote && this.food != 0) {
            
            List<EntityAnimal> animals = EntityUtils.getEntitiesInArea(EntityAnimal.class, this.getWorld(), this.getPos(), 8);
            
            for (EntityAnimal animal : animals) {
                
                if (animal.getGrowingAge() == 0 && animal.inLove <= 0) {
                    
                    animal.inLove = 1200;
                    this.getWorld().setEntityState(animal, (byte) 18);
                }
            }
        }
    }
    
    public IBlockState getStateFromFood () {
        
        return ContentHandler.blockFeeder.getStateFromMeta(food);
    }
    
    @Override
    public void writeNBT (NBTTagCompound dataTag) {
        
        dataTag.setInteger("FoodAmount", this.food);
    }
    
    @Override
    public void readNBT (NBTTagCompound dataTag) {
        
        this.food = dataTag.getInteger("FoodAmount");
    }
    
    @Override
    public String getName () {
        
        return "tile.darkutils.feeder.name";
    }
    
    @Override
    public boolean hasCustomName () {
        
        return false;
    }
    
    @Override
    public IChatComponent getDisplayName () {
        
        return null;
    }
    
    @Override
    public int getSizeInventory () {
        
        return 10;
    }
    
    @Override
    public ItemStack getStackInSlot (int index) {
        
        return null;
    }
    
    @Override
    public ItemStack decrStackSize (int index, int count) {
        
        return null;
    }
    
    @Override
    public ItemStack removeStackFromSlot (int index) {
        
        return null;
    }
    
    @Override
    public void setInventorySlotContents (int index, ItemStack stack) {
        
        stack.stackSize = addFood(stack);
    }
    
    @Override
    public int getInventoryStackLimit () {
        
        return 1;
    }
    
    @Override
    public boolean isUseableByPlayer (EntityPlayer player) {
        
        return false;
    }
    
    @Override
    public void openInventory (EntityPlayer player) {
    
    }
    
    @Override
    public void closeInventory (EntityPlayer player) {
    
    }
    
    @Override
    public boolean isItemValidForSlot (int index, ItemStack stack) {
        
        if (this.food != 10 && ItemStackUtils.isValidStack(stack)) {
            
            Item item = stack.getItem();
            return (item == Items.carrot || item == Items.wheat_seeds || item == Items.wheat);
        }
        
        return false;
    }
    
    @Override
    public int getField (int id) {
        
        return 0;
    }
    
    @Override
    public void setField (int id, int value) {
    
    }
    
    @Override
    public int getFieldCount () {
        
        return 0;
    }
    
    @Override
    public void clear () {
        
        setFood(0);
    }
}