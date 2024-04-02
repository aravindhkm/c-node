package org.tron.api;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.52.1)",
    comments = "Source: api/api.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class WalletExtensionGrpc {

  private WalletExtensionGrpc() {}

  public static final String SERVICE_NAME = "protocol.WalletExtension";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated,
      org.tron.api.GrpcAPI.TransactionList> getGetTransactionsFromThisMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTransactionsFromThis",
      requestType = org.tron.api.GrpcAPI.AccountPaginated.class,
      responseType = org.tron.api.GrpcAPI.TransactionList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated,
      org.tron.api.GrpcAPI.TransactionList> getGetTransactionsFromThisMethod() {
    io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated, org.tron.api.GrpcAPI.TransactionList> getGetTransactionsFromThisMethod;
    if ((getGetTransactionsFromThisMethod = WalletExtensionGrpc.getGetTransactionsFromThisMethod) == null) {
      synchronized (WalletExtensionGrpc.class) {
        if ((getGetTransactionsFromThisMethod = WalletExtensionGrpc.getGetTransactionsFromThisMethod) == null) {
          WalletExtensionGrpc.getGetTransactionsFromThisMethod = getGetTransactionsFromThisMethod =
              io.grpc.MethodDescriptor.<org.tron.api.GrpcAPI.AccountPaginated, org.tron.api.GrpcAPI.TransactionList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTransactionsFromThis"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.GrpcAPI.AccountPaginated.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.GrpcAPI.TransactionList.getDefaultInstance()))
              .setSchemaDescriptor(new WalletExtensionMethodDescriptorSupplier("GetTransactionsFromThis"))
              .build();
        }
      }
    }
    return getGetTransactionsFromThisMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated,
      org.tron.api.GrpcAPI.TransactionListExtention> getGetTransactionsFromThis2Method;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTransactionsFromThis2",
      requestType = org.tron.api.GrpcAPI.AccountPaginated.class,
      responseType = org.tron.api.GrpcAPI.TransactionListExtention.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated,
      org.tron.api.GrpcAPI.TransactionListExtention> getGetTransactionsFromThis2Method() {
    io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated, org.tron.api.GrpcAPI.TransactionListExtention> getGetTransactionsFromThis2Method;
    if ((getGetTransactionsFromThis2Method = WalletExtensionGrpc.getGetTransactionsFromThis2Method) == null) {
      synchronized (WalletExtensionGrpc.class) {
        if ((getGetTransactionsFromThis2Method = WalletExtensionGrpc.getGetTransactionsFromThis2Method) == null) {
          WalletExtensionGrpc.getGetTransactionsFromThis2Method = getGetTransactionsFromThis2Method =
              io.grpc.MethodDescriptor.<org.tron.api.GrpcAPI.AccountPaginated, org.tron.api.GrpcAPI.TransactionListExtention>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTransactionsFromThis2"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.GrpcAPI.AccountPaginated.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.GrpcAPI.TransactionListExtention.getDefaultInstance()))
              .setSchemaDescriptor(new WalletExtensionMethodDescriptorSupplier("GetTransactionsFromThis2"))
              .build();
        }
      }
    }
    return getGetTransactionsFromThis2Method;
  }

  private static volatile io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated,
      org.tron.api.GrpcAPI.TransactionList> getGetTransactionsToThisMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTransactionsToThis",
      requestType = org.tron.api.GrpcAPI.AccountPaginated.class,
      responseType = org.tron.api.GrpcAPI.TransactionList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated,
      org.tron.api.GrpcAPI.TransactionList> getGetTransactionsToThisMethod() {
    io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated, org.tron.api.GrpcAPI.TransactionList> getGetTransactionsToThisMethod;
    if ((getGetTransactionsToThisMethod = WalletExtensionGrpc.getGetTransactionsToThisMethod) == null) {
      synchronized (WalletExtensionGrpc.class) {
        if ((getGetTransactionsToThisMethod = WalletExtensionGrpc.getGetTransactionsToThisMethod) == null) {
          WalletExtensionGrpc.getGetTransactionsToThisMethod = getGetTransactionsToThisMethod =
              io.grpc.MethodDescriptor.<org.tron.api.GrpcAPI.AccountPaginated, org.tron.api.GrpcAPI.TransactionList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTransactionsToThis"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.GrpcAPI.AccountPaginated.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.GrpcAPI.TransactionList.getDefaultInstance()))
              .setSchemaDescriptor(new WalletExtensionMethodDescriptorSupplier("GetTransactionsToThis"))
              .build();
        }
      }
    }
    return getGetTransactionsToThisMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated,
      org.tron.api.GrpcAPI.TransactionListExtention> getGetTransactionsToThis2Method;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTransactionsToThis2",
      requestType = org.tron.api.GrpcAPI.AccountPaginated.class,
      responseType = org.tron.api.GrpcAPI.TransactionListExtention.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated,
      org.tron.api.GrpcAPI.TransactionListExtention> getGetTransactionsToThis2Method() {
    io.grpc.MethodDescriptor<org.tron.api.GrpcAPI.AccountPaginated, org.tron.api.GrpcAPI.TransactionListExtention> getGetTransactionsToThis2Method;
    if ((getGetTransactionsToThis2Method = WalletExtensionGrpc.getGetTransactionsToThis2Method) == null) {
      synchronized (WalletExtensionGrpc.class) {
        if ((getGetTransactionsToThis2Method = WalletExtensionGrpc.getGetTransactionsToThis2Method) == null) {
          WalletExtensionGrpc.getGetTransactionsToThis2Method = getGetTransactionsToThis2Method =
              io.grpc.MethodDescriptor.<org.tron.api.GrpcAPI.AccountPaginated, org.tron.api.GrpcAPI.TransactionListExtention>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTransactionsToThis2"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.GrpcAPI.AccountPaginated.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.GrpcAPI.TransactionListExtention.getDefaultInstance()))
              .setSchemaDescriptor(new WalletExtensionMethodDescriptorSupplier("GetTransactionsToThis2"))
              .build();
        }
      }
    }
    return getGetTransactionsToThis2Method;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WalletExtensionStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletExtensionStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletExtensionStub>() {
        @java.lang.Override
        public WalletExtensionStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletExtensionStub(channel, callOptions);
        }
      };
    return WalletExtensionStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WalletExtensionBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletExtensionBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletExtensionBlockingStub>() {
        @java.lang.Override
        public WalletExtensionBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletExtensionBlockingStub(channel, callOptions);
        }
      };
    return WalletExtensionBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WalletExtensionFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletExtensionFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletExtensionFutureStub>() {
        @java.lang.Override
        public WalletExtensionFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletExtensionFutureStub(channel, callOptions);
        }
      };
    return WalletExtensionFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class WalletExtensionImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *Please use GetTransactionsFromThis2 instead of this function.
     * </pre>
     */
    public void getTransactionsFromThis(org.tron.api.GrpcAPI.AccountPaginated request,
        io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTransactionsFromThisMethod(), responseObserver);
    }

    /**
     * <pre>
     *Use this function instead of GetTransactionsFromThis.
     * </pre>
     */
    public void getTransactionsFromThis2(org.tron.api.GrpcAPI.AccountPaginated request,
        io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionListExtention> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTransactionsFromThis2Method(), responseObserver);
    }

    /**
     * <pre>
     *Please use GetTransactionsToThis2 instead of this function.
     * </pre>
     */
    public void getTransactionsToThis(org.tron.api.GrpcAPI.AccountPaginated request,
        io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTransactionsToThisMethod(), responseObserver);
    }

    /**
     * <pre>
     *Use this function instead of GetTransactionsToThis.
     * </pre>
     */
    public void getTransactionsToThis2(org.tron.api.GrpcAPI.AccountPaginated request,
        io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionListExtention> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTransactionsToThis2Method(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetTransactionsFromThisMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.tron.api.GrpcAPI.AccountPaginated,
                org.tron.api.GrpcAPI.TransactionList>(
                  this, METHODID_GET_TRANSACTIONS_FROM_THIS)))
          .addMethod(
            getGetTransactionsFromThis2Method(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.tron.api.GrpcAPI.AccountPaginated,
                org.tron.api.GrpcAPI.TransactionListExtention>(
                  this, METHODID_GET_TRANSACTIONS_FROM_THIS2)))
          .addMethod(
            getGetTransactionsToThisMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.tron.api.GrpcAPI.AccountPaginated,
                org.tron.api.GrpcAPI.TransactionList>(
                  this, METHODID_GET_TRANSACTIONS_TO_THIS)))
          .addMethod(
            getGetTransactionsToThis2Method(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.tron.api.GrpcAPI.AccountPaginated,
                org.tron.api.GrpcAPI.TransactionListExtention>(
                  this, METHODID_GET_TRANSACTIONS_TO_THIS2)))
          .build();
    }
  }

  /**
   */
  public static final class WalletExtensionStub extends io.grpc.stub.AbstractAsyncStub<WalletExtensionStub> {
    private WalletExtensionStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletExtensionStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletExtensionStub(channel, callOptions);
    }

    /**
     * <pre>
     *Please use GetTransactionsFromThis2 instead of this function.
     * </pre>
     */
    public void getTransactionsFromThis(org.tron.api.GrpcAPI.AccountPaginated request,
        io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTransactionsFromThisMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Use this function instead of GetTransactionsFromThis.
     * </pre>
     */
    public void getTransactionsFromThis2(org.tron.api.GrpcAPI.AccountPaginated request,
        io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionListExtention> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTransactionsFromThis2Method(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Please use GetTransactionsToThis2 instead of this function.
     * </pre>
     */
    public void getTransactionsToThis(org.tron.api.GrpcAPI.AccountPaginated request,
        io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTransactionsToThisMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Use this function instead of GetTransactionsToThis.
     * </pre>
     */
    public void getTransactionsToThis2(org.tron.api.GrpcAPI.AccountPaginated request,
        io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionListExtention> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTransactionsToThis2Method(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class WalletExtensionBlockingStub extends io.grpc.stub.AbstractBlockingStub<WalletExtensionBlockingStub> {
    private WalletExtensionBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletExtensionBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletExtensionBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *Please use GetTransactionsFromThis2 instead of this function.
     * </pre>
     */
    public org.tron.api.GrpcAPI.TransactionList getTransactionsFromThis(org.tron.api.GrpcAPI.AccountPaginated request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTransactionsFromThisMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Use this function instead of GetTransactionsFromThis.
     * </pre>
     */
    public org.tron.api.GrpcAPI.TransactionListExtention getTransactionsFromThis2(org.tron.api.GrpcAPI.AccountPaginated request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTransactionsFromThis2Method(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Please use GetTransactionsToThis2 instead of this function.
     * </pre>
     */
    public org.tron.api.GrpcAPI.TransactionList getTransactionsToThis(org.tron.api.GrpcAPI.AccountPaginated request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTransactionsToThisMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Use this function instead of GetTransactionsToThis.
     * </pre>
     */
    public org.tron.api.GrpcAPI.TransactionListExtention getTransactionsToThis2(org.tron.api.GrpcAPI.AccountPaginated request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTransactionsToThis2Method(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class WalletExtensionFutureStub extends io.grpc.stub.AbstractFutureStub<WalletExtensionFutureStub> {
    private WalletExtensionFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletExtensionFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletExtensionFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *Please use GetTransactionsFromThis2 instead of this function.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tron.api.GrpcAPI.TransactionList> getTransactionsFromThis(
        org.tron.api.GrpcAPI.AccountPaginated request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTransactionsFromThisMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Use this function instead of GetTransactionsFromThis.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tron.api.GrpcAPI.TransactionListExtention> getTransactionsFromThis2(
        org.tron.api.GrpcAPI.AccountPaginated request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTransactionsFromThis2Method(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Please use GetTransactionsToThis2 instead of this function.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tron.api.GrpcAPI.TransactionList> getTransactionsToThis(
        org.tron.api.GrpcAPI.AccountPaginated request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTransactionsToThisMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Use this function instead of GetTransactionsToThis.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tron.api.GrpcAPI.TransactionListExtention> getTransactionsToThis2(
        org.tron.api.GrpcAPI.AccountPaginated request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTransactionsToThis2Method(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_TRANSACTIONS_FROM_THIS = 0;
  private static final int METHODID_GET_TRANSACTIONS_FROM_THIS2 = 1;
  private static final int METHODID_GET_TRANSACTIONS_TO_THIS = 2;
  private static final int METHODID_GET_TRANSACTIONS_TO_THIS2 = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final WalletExtensionImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(WalletExtensionImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_TRANSACTIONS_FROM_THIS:
          serviceImpl.getTransactionsFromThis((org.tron.api.GrpcAPI.AccountPaginated) request,
              (io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionList>) responseObserver);
          break;
        case METHODID_GET_TRANSACTIONS_FROM_THIS2:
          serviceImpl.getTransactionsFromThis2((org.tron.api.GrpcAPI.AccountPaginated) request,
              (io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionListExtention>) responseObserver);
          break;
        case METHODID_GET_TRANSACTIONS_TO_THIS:
          serviceImpl.getTransactionsToThis((org.tron.api.GrpcAPI.AccountPaginated) request,
              (io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionList>) responseObserver);
          break;
        case METHODID_GET_TRANSACTIONS_TO_THIS2:
          serviceImpl.getTransactionsToThis2((org.tron.api.GrpcAPI.AccountPaginated) request,
              (io.grpc.stub.StreamObserver<org.tron.api.GrpcAPI.TransactionListExtention>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class WalletExtensionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WalletExtensionBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.tron.api.GrpcAPI.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WalletExtension");
    }
  }

  private static final class WalletExtensionFileDescriptorSupplier
      extends WalletExtensionBaseDescriptorSupplier {
    WalletExtensionFileDescriptorSupplier() {}
  }

  private static final class WalletExtensionMethodDescriptorSupplier
      extends WalletExtensionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    WalletExtensionMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (WalletExtensionGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WalletExtensionFileDescriptorSupplier())
              .addMethod(getGetTransactionsFromThisMethod())
              .addMethod(getGetTransactionsFromThis2Method())
              .addMethod(getGetTransactionsToThisMethod())
              .addMethod(getGetTransactionsToThis2Method())
              .build();
        }
      }
    }
    return result;
  }
}
