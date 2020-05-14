/*
 * arcus-java-client : Arcus Java client
 * Copyright 2015 JaM2in Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.spy.memcached;

import junit.framework.Assert;
import net.spy.memcached.collection.*;
import net.spy.memcached.ops.*;
import net.spy.memcached.protocol.ascii.AsciiOperationFactory;
import net.spy.memcached.transcoders.CollectionTranscoder;
import net.spy.memcached.transcoders.IntegerTranscoder;
import net.spy.memcached.transcoders.Transcoder;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class MultibyteKeyTest {
  private static final String MULTIBYTE_KEY = "아커스프리픽스:아커스멀티바이트키스트링";
  private final AsciiOperationFactory opFact = new AsciiOperationFactory();
  private byte[] testData;
  private OperationCallback genericCallback = null;
  private List<String> keyList = null;

  @Before
  public void setUp() throws Exception {
    genericCallback = new OperationCallback() {
      @Override
      public void receivedStatus(OperationStatus status) {
      }

      @Override
      public void complete() {
      }
    };
    testData = new byte[64];
    new Random().nextBytes(testData);
    keyList = new ArrayList<String>();
    for (int i = 0; i < 10; i++) {
      keyList.add(MULTIBYTE_KEY + String.valueOf(i));
    }
  }

  @Test
  public void getsTest() {
    try {
      opFact.gets(MULTIBYTE_KEY, new GetsOperation.Callback() {
        @Override
        public void receivedStatus(OperationStatus status) {
        }

        @Override
        public void complete() {
        }

        @Override
        public void gotData(String key, int flags, long cas, byte[] data) {
        }
      }).initialize();        // BaseGetOpImpl.initialize()
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void collectionInsertOperationImplTest() {
    try {
      opFact.collectionInsert(MULTIBYTE_KEY, MULTIBYTE_KEY, new CollectionInsert<Integer>() {
        @Override
        public String getCommand() {
          return null;
        }
      }, testData, genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void collectionGetOperationImplTest() {
    try {
      opFact.collectionGet(MULTIBYTE_KEY, new CollectionGet() {
        @Override
        public String stringify() {
          return "collectionGetString";
        }

        @Override
        public byte[] getAddtionalArgs() {
          return null;
        }

        @Override
        public String getCommand() {
          return "collectionGetCommand";
        }

        @Override
        public void decodeItemHeader(String itemHeader) {
        }
      }, new CollectionGetOperation.Callback() {
        @Override
        public void gotData(String key, String subkey, int flags, byte[] data) {
        }

        @Override
        public void receivedStatus(OperationStatus status) {
        }

        @Override
        public void complete() {
        }
      }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CASOperationImplTest() {
    try {
      opFact.cas(StoreType.add, MULTIBYTE_KEY, 1L, 0, 0, testData, genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void ConcatenationOperationTest() {
    try {
      opFact.cat(ConcatenationType.append, 1L, MULTIBYTE_KEY, testData, genericCallback)
          .initialize();
      //BaseStoreOperationImpl.initialize()
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void BTreeFindPositionWithGetOperationImplTest() {
    try {
      opFact.bopFindPositionWithGet(MULTIBYTE_KEY,
          new BTreeFindPositionWithGet(1L, BTreeOrder.ASC, 0),
          genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void SetExistOperationImplTest() {
    try {
      Transcoder<Object> tc = new CollectionTranscoder();
      opFact.collectionExist(MULTIBYTE_KEY, "",
          new SetExist<Object>(new Random().nextInt(), tc) {
            @Override
            public String getCommand() {
              return "CollectionExistCommand";
            }
          }, genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionMutateOperationImplTest() {
    try {
      opFact.collectionMutate(MULTIBYTE_KEY, "",
          new CollectionMutate() {
            @Override
            public String stringify() {
              return "CollectionMutateString";
            }

            @Override
            public String getCommand() {
              return "CollectionMutateCommand";
            }
          }, genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void getAttrTest() {
    try {
      opFact.getAttr(MULTIBYTE_KEY, new GetAttrOperation.Callback() {
        @Override
        public void gotAttribute(String key, String attr) {
        }

        @Override
        public void receivedStatus(OperationStatus status) {
        }

        @Override
        public void complete() {
        }
      }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void MutatorOperationImplTest() {
    try {
      opFact.mutate(Mutator.incr, MULTIBYTE_KEY, 1, 1L, 0, genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void BTreeSortMergeGetOperationImplTest() {
    try {
      opFact.bopsmget(
          new BTreeSMGetWithLongTypeBkey<Object>(
              keyList, 0L, 100L, ElementFlagFilter.DO_NOT_FILTER,0, SMGetMode.UNIQUE),
          new BTreeSortMergeGetOperation.Callback() {
            @Override
            public void gotData(String key, Object subkey, int flags, byte[] data) {
            }

            @Override
            public void gotMissedKey(String key, OperationStatus cause) {
            }

            @Override
            public void gotTrimmedKey(String key, Object subkey) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void BTreeSortMergeGetOperationOldImplTest() {
    try {
      opFact.bopsmget(
          new BTreeSMGetWithLongTypeBkeyOld<Object>(
              keyList, 0L, 100L, ElementFlagFilter.DO_NOT_FILTER,0, 0),
          new BTreeSortMergeGetOperationOld.Callback() {
            @Override
            public void gotData(String key, Object subkey, int flags, byte[] data) {
            }

            @Override
            public void gotMissedKey(byte[] data) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionPipedInsertOperationImplTest() {
    CollectionPipedInsertOperation.Callback cpsCallback =
        new CollectionPipedInsertOperation.Callback() {
          @Override
          public void gotStatus(Integer index, OperationStatus status) {
          }

          @Override
          public void receivedStatus(OperationStatus status) {
          }

          @Override
          public void complete() {
          }
        };

    List<Element<Integer>> elements = new ArrayList<Element<Integer>>();
    for (int i = 0; i < 10; i++) {
      elements.add(new Element<Integer>(Long.valueOf(i), new Random().nextInt(), new byte[]{1, 1}));
    }
    CollectionPipedInsert<Integer> insert =
        new CollectionPipedInsert.ByteArraysBTreePipedInsert<Integer>(
            MULTIBYTE_KEY, elements, false, new CollectionAttributes(),
            new IntegerTranscoder());
    try {
      opFact.collectionPipedInsert(MULTIBYTE_KEY, insert, cpsCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }

    Map<Long, Integer> elementsMap = new HashMap<Long, Integer>();
    for (int i = 0; i < 10; i++) {
      elementsMap.put(Long.valueOf(i), new Random().nextInt());
    }
    insert = new CollectionPipedInsert.BTreePipedInsert<Integer>(
            MULTIBYTE_KEY, elementsMap, false, new CollectionAttributes(),
            new IntegerTranscoder());
    try {
      opFact.collectionPipedInsert(MULTIBYTE_KEY, insert, cpsCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }

    List<Integer> elementsList = new ArrayList<Integer>();
    for (int i = 0; i < 10; i++) {
      elementsList.add(new Random().nextInt());
    }
    insert = new CollectionPipedInsert.ListPipedInsert<Integer>(
            MULTIBYTE_KEY, 0, elementsList, false, new CollectionAttributes(),
            new IntegerTranscoder());
    try {
      opFact.collectionPipedInsert(MULTIBYTE_KEY, insert, cpsCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }

    Set<Integer> elementsSet = new HashSet<Integer>();
    for (int i = 0; i < 10; i++) {
      elementsSet.add(new Random().nextInt());
    }
    insert = new CollectionPipedInsert.SetPipedInsert<Integer>(
            MULTIBYTE_KEY, elementsSet, false, new CollectionAttributes(), new IntegerTranscoder());
    try {
      opFact.collectionPipedInsert(MULTIBYTE_KEY, insert, cpsCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void DeleteOperationImplTest() {
    try {
      opFact.delete(MULTIBYTE_KEY, genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void ExtendedBTreeGetOperationImplTest() {
    byte[] from = new byte[]{0, 0};
    byte[] to = new byte[]{10, 10};
    try {
      opFact.collectionGet(MULTIBYTE_KEY,
          new BTreeGet(from, to, 0, 0, false, false, ElementFlagFilter.DO_NOT_FILTER),
          new CollectionGetOperation.Callback() {
            @Override
            public void gotData(String key, String subkey, int flags, byte[] data) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionDeleteOperationImplTest() {
    try {
      opFact.collectionDelete(MULTIBYTE_KEY,
          new BTreeDelete(1L, false),
          new OperationCallback() {
            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionBulkInsertOperationImplTest() {
    CollectionBulkInsert<Integer> insert = null;
    CollectionBulkInsertOperation.Callback cbsCallback =
        new CollectionBulkInsertOperation.Callback() {
          @Override
          public void gotStatus(Integer index, OperationStatus status) {
          }

          @Override
          public void receivedStatus(OperationStatus status) {
          }

          @Override
          public void complete() {
          }
        };

    insert = new CollectionBulkInsert.BTreeBulkInsert<Integer>(keyList, 1L, new byte[]{0, 0},
            new Random().nextInt(), new CollectionAttributes(), new IntegerTranscoder());
    try {
      opFact.collectionBulkInsert(insert.getKeyList(), insert, cbsCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }

    insert = new CollectionBulkInsert.ListBulkInsert<Integer>(keyList, 0, new Random().nextInt(),
            new CollectionAttributes(), new IntegerTranscoder());

    try {
      opFact.collectionBulkInsert(insert.getKeyList(), insert, cbsCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }

    insert = new CollectionBulkInsert.SetBulkInsert<Integer>(keyList, new Random().nextInt(),
            new CollectionAttributes(), new IntegerTranscoder());
    try {
      opFact.collectionBulkInsert(insert.getKeyList(), insert, cbsCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void BTreeGetBulkOperationImplTest() {
    try {
      opFact.bopGetBulk(
          new BTreeGetBulkWithLongTypeBkey<Integer>(
              keyList, 0L, 10L, ElementFlagFilter.DO_NOT_FILTER,0, 0
          ),
          new BTreeGetBulkOperation.Callback<Integer>() {
            @Override
            public void gotElement(String key, Object subkey,
                                   int flags, byte[] eflag, byte[] data) {
            }

            @Override
            public void gotKey(String key, int elementCount, OperationStatus status) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void BTreeGetByPositionOperationImplTest() {
    try {
      opFact.bopGetByPosition(MULTIBYTE_KEY,
          new BTreeGetByPosition(BTreeOrder.ASC, 0),
          new BTreeGetByPositionOperation.Callback() {
            @Override
            public void gotData(String key, int flags, int pos, BKeyObject bkey,
                                byte[] eflag, byte[] data) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionUpdateOperationImplTest() {
    try {
      opFact.collectionUpdate(MULTIBYTE_KEY, MULTIBYTE_KEY,
              new BTreeUpdate<Integer>(new Random().nextInt(), ElementFlagUpdate.RESET_FLAG, false),
              new byte[]{0, 0}, genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionPipedUpdateOperationImplTest() {
    List<Element<Integer>> elementsList = new ArrayList<Element<Integer>>();
    for (int i = 0; i < 10; i++) {
      elementsList.add(new Element<Integer>(
          Long.valueOf(i), new Random().nextInt(), ElementFlagUpdate.RESET_FLAG));
    }
    try {
      opFact.collectionPipedUpdate(MULTIBYTE_KEY,
          new CollectionPipedUpdate.BTreePipedUpdate<Integer>(
              MULTIBYTE_KEY, elementsList, new IntegerTranscoder()),
          new CollectionPipedUpdateOperation.Callback() {
            @Override
            public void gotStatus(Integer index, OperationStatus status) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionCreateOperationImplTest() {
    try {
      opFact.collectionCreate(MULTIBYTE_KEY,
              new BTreeCreate(0, 0, 10000L, CollectionOverflowAction.error, true, false),
              genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionCountOperationImplTest() {
    try {
      opFact.collectionCount(MULTIBYTE_KEY,
              new BTreeCount(0L, 10L, ElementFlagFilter.DO_NOT_FILTER),
              genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void CollectionPipedExistOperationImplTest() {
    List<Integer> objectList = new ArrayList<Integer>();
    for (int i = 0; i < 10; i++) {
      objectList.add(new Random().nextInt());
    }
    try {
      opFact.collectionPipedExist(MULTIBYTE_KEY,
          new SetPipedExist<Integer>(MULTIBYTE_KEY, objectList, new IntegerTranscoder()),
          new CollectionPipedExistOperation.Callback() {
            @Override
            public void gotStatus(Integer index, OperationStatus status) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void BTreeInsertAndGetOperationImplTest() {
    try {
      opFact.bopInsertAndGet(MULTIBYTE_KEY,
          new BTreeInsertAndGet<Integer>(BTreeInsertAndGet.Command.INSERT, 1L, new byte[]{0, 0},
                  new Random().nextInt(), new CollectionAttributes()),
          testData, new BTreeInsertAndGetOperation.Callback() {
            @Override
            public void gotData(String key, int flags, BKeyObject bkeyObject,
                                byte[] elementFlag, byte[] data) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void SetAttrOperationImplTest() {
    try {
      opFact.setAttr(MULTIBYTE_KEY, new Attributes(), genericCallback).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }

  @Test
  public void BTreeFindPositionOperationImplTest() {
    try {
      opFact.bopFindPosition(MULTIBYTE_KEY,
          new BTreeFindPosition(1L, BTreeOrder.ASC),
          new BTreeFindPositionOperation.Callback() {
            @Override
            public void gotData(int position) {
            }

            @Override
            public void receivedStatus(OperationStatus status) {
            }

            @Override
            public void complete() {
            }
          }).initialize();
    } catch (java.nio.BufferOverflowException e) {
      Assert.fail();
    }
  }
}
